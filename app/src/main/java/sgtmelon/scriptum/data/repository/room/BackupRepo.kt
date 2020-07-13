package sgtmelon.scriptum.data.repository.room

import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendarOrNull
import sgtmelon.extension.getText
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import java.util.*

/**
 * Repository of [RoomDb] which work with backup data.
 */
class BackupRepo(
        override val roomProvider: RoomProvider,
        private val noteConverter: NoteConverter,
        private val rollConverter: RollConverter,
        private val rankConverter: RankConverter,
        private val alarmConverter: AlarmConverter
) : IBackupRepo,
        IRoomWork {

    // TODO update notification bind's
    override suspend fun insertData(parserResult: ParserResult,
                                    importSkip: Boolean): ImportResult = takeFromRoom {
        // todo backup item with converting from [parserResult].
        val noteList = parserResult.noteList.toMutableList()
        val rollList = parserResult.rollList.toMutableList()
        val rollVisibleList = parserResult.rollVisibleList.toMutableList()
        val rankList = parserResult.rankList.toMutableList()
        val alarmList = parserResult.alarmList.toMutableList()

        if (importSkip) {
            val removeNoteList = getRemoveNoteList(noteList, rollList, roomDb = this)
            clearList(removeNoteList, noteList, rollList, rollVisibleList, rankList, alarmList)
        }

        rankList.removeAll(getRemoveRankList(rankList, noteList, roomDb = this))
        alarmList.removeAll(getRemoveAlarmList(alarmList, roomDb = this))

        insertNoteList(noteList, rollList, rollVisibleList, rankList, alarmList, roomDb = this)
        insertRollList(rollList, roomDb = this)
        insertRollVisibleList(rollVisibleList, roomDb = this)
        insertRankList(rankList, noteList, roomDb = this)
        insertAlarmList(alarmList, roomDb = this)

        TODO()

        return@takeFromRoom if (importSkip) {
            ImportResult.Skip(skipCount = parserResult.noteList.size - noteList.size)
        } else {
            ImportResult.Simple
        }
    }

    /**
     * Return list for remove with items from [noteList], which already exists in [roomDb].
     */
    @RunPrivate
    suspend fun getRemoveNoteList(noteList: List<NoteEntity>, rollList: List<RollEntity>,
                                  roomDb: RoomDb): List<NoteEntity> {
        val removeList = mutableListOf<NoteEntity>()

        val existNoteList = roomDb.noteDao.get(bin = false)
        val existRollNoteList = existNoteList.filter { it.type == NoteType.ROLL }

        for (item in noteList) {
            when (item.type) {
                NoteType.TEXT -> if (needSkipTextNote(item, existNoteList)) removeList.add(item)
                NoteType.ROLL -> {
                    val itemRollList = rollList.filter { it.noteId == item.id }
                    if (needSkipRollNote(item, itemRollList, existRollNoteList, roomDb)) {
                        removeList.add(item)
                    }
                }
            }
        }

        return removeList
    }

    /**
     * Check condition for skip [NoteType.TEXT].
     */
    @RunPrivate
    fun needSkipTextNote(item: NoteEntity, existNoteList: List<NoteEntity>): Boolean {
        return existNoteList.any { it.name == item.name && it.text == item.text }
    }

    /**
     * Check condition for skip [NoteType.ROLL].
     */
    @RunPrivate
    suspend fun needSkipRollNote(item: NoteEntity, rollList: List<RollEntity>,
                                 existNoteList: List<NoteEntity>, roomDb: RoomDb): Boolean {
        for (existItem in existNoteList.filter { it.name == item.name }) {
            val existRollList = roomDb.rollDao.get(existItem.id)

            if (rollList.size != existRollList.size) continue

            var needSkip = true
            for (rollItem in rollList) {
                if (!existRollList.any { it.text == rollItem.text }) {
                    needSkip = false
                    break
                }
            }

            if (needSkip) return true
        }

        return false
    }

    /**
     * Remove every mention about items of [removeNoteList] inside lists.
     */
    @RunPrivate
    fun clearList(removeNoteList: List<NoteEntity>,
                  noteList: MutableList<NoteEntity>,
                  rollList: MutableList<RollEntity>,
                  rollVisibleList: MutableList<RollVisibleEntity>,
                  rankList: MutableList<RankEntity>,
                  alarmList: MutableList<AlarmEntity>) {
        for (item in removeNoteList) {
            if (item.type == NoteType.ROLL) {
                rollList.removeAll { it.noteId == item.id }
                rollVisibleList.removeAll { it.noteId == item.id }
            }

            rankList.filter { it.noteId.contains(item.id) }.forEach {
                it.noteId.remove(item.id)
            }

            alarmList.removeAll { it.noteId == item.id }
        }

        noteList.removeAll(removeNoteList)
    }

    /**
     * Return list for remove with items from [rankList], which already exists in [roomDb].
     * Also update [NoteEntity.rankId] and [NoteEntity.rankPs] (if need) for items in [noteList].
     */
    @RunPrivate
    suspend fun getRemoveRankList(rankList: List<RankEntity>, noteList: List<NoteEntity>,
                                  roomDb: RoomDb): List<RankEntity> {
        val removeList = mutableListOf<RankEntity>()

        val existRankList = roomDb.rankDao.get()

        for (item in rankList) {
            val index = existRankList.indexOfFirst { it.name == item.name }
            val existItem = existRankList.getOrNull(index) ?: continue

            removeList.add(item)

            noteList.filter { it.rankId == item.id }.forEach {
                it.rankId = existItem.id
                it.rankPs = index
            }
        }

        return removeList
    }

    /**
     * Return list for remove with items from [alarmList], which already past.
     * Also change time of [alarmList] items, if user have same date in [roomDb].
     */
    @RunPrivate
    suspend fun getRemoveAlarmList(alarmList: List<AlarmEntity>,
                                   roomDb: RoomDb): List<AlarmEntity> {
        val removeList = mutableListOf<AlarmEntity>()

        val notificationList = roomDb.alarmDao.getList()

        for (item in alarmList) {
            val calendar = item.date.getCalendarOrNull() ?: continue

            if (calendar.beforeNow()) {
                removeList.add(item)
                continue
            }

            while (notificationList.any { it.alarm.date == item.date }) {
                item.date = calendar.apply { add(Calendar.MINUTE, 1) }.getText()
            }
        }

        return removeList
    }

    /**
     * Insert notes from [noteList] to [roomDb].
     * Also update (if need) noteId inside other list items.
     */
    @RunPrivate
    suspend fun insertNoteList(noteList: List<NoteEntity>,
                               rollList: List<RollEntity>,
                               rollVisibleList: List<RollVisibleEntity>,
                               rankList: List<RankEntity>,
                               alarmList: List<AlarmEntity>,
                               roomDb: RoomDb) {
        noteList.forEach { item ->
            val oldId = item.id

            /**
             * Need reset [NoteEntity.id] for prevent unique id exception.
             */
            item.id = roomDb.noteDao.insert(item.apply { id = Note.Default.ID })

            rollList.filter { it.noteId == oldId }.forEach { it.noteId = item.id }
            rollVisibleList.filter { it.noteId == oldId }.forEach { it.noteId = item.id }

            rankList.filter { it.noteId.contains(oldId) }.forEach {
                it.noteId.remove(oldId)
                it.noteId.add(item.id)
            }

            alarmList.filter { it.noteId == oldId }.forEach { it.noteId = item.id }
        }
    }

    @RunPrivate
    suspend fun insertRollList(rollList: List<RollEntity>, roomDb: RoomDb) {
        TODO("Not yet implemented")
    }

    @RunPrivate
    suspend fun insertRollVisibleList(rollVisibleList: List<RollVisibleEntity>, roomDb: RoomDb) {
        TODO("Not yet implemented")
    }

    @RunPrivate
    suspend fun insertRankList(rankList: List<RankEntity>, noteList: List<NoteEntity>, roomDb: RoomDb) {
        TODO("Not yet implemented")
    }

    @RunPrivate
    suspend fun insertAlarmList(alarmList: List<AlarmEntity>, roomDb: RoomDb) {
        TODO("Not yet implemented")
    }

}