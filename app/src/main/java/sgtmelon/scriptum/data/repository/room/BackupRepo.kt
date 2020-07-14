package sgtmelon.scriptum.data.repository.room

import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendarOrNull
import sgtmelon.extension.getText
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.data.DbData.Rank
import sgtmelon.scriptum.domain.model.data.DbData.Roll
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import java.util.*

/**
 * Repository of [RoomDb] which work with backup data.
 */
class BackupRepo(override val roomProvider: RoomProvider) : IBackupRepo,
        IRoomWork {

    class Model(
            val noteList: MutableList<NoteEntity>,
            val rollList: MutableList<RollEntity>,
            val rollVisibleList: MutableList<RollVisibleEntity>,
            val rankList: MutableList<RankEntity>,
            val alarmList: MutableList<AlarmEntity>
    ) {

        constructor(result: ParserResult): this(
                result.noteList.toMutableList(),
                result.rollList.toMutableList(),
                result.rollVisibleList.toMutableList(),
                result.rankList.toMutableList(),
                result.alarmList.toMutableList()
        )
    }

    override suspend fun insertData(parserResult: ParserResult,
                                    importSkip: Boolean): ImportResult = takeFromRoom {
        val model = Model(parserResult)

        if (importSkip) {
            val removeNoteList = getRemoveNoteList(model, roomDb = this)
            clearList(removeNoteList, model)
        }

        clearRankList(model, roomDb = this)
        clearAlarmList(model = model, roomDb = this)

        insertNoteList(model, roomDb = this)
        insertRollList(model, roomDb = this)
        insertRollVisibleList(model, roomDb = this)
        insertRankList(model, roomDb = this)
        insertAlarmList(model, roomDb = this)

        return@takeFromRoom if (importSkip) {
            ImportResult.Skip(skipCount = parserResult.noteList.size - model.noteList.size)
        } else {
            ImportResult.Simple
        }
    }

    /**
     * Return list for remove with items from [Model.noteList], which already exists in [roomDb].
     */
    @RunPrivate
    suspend fun getRemoveNoteList(model: Model, roomDb: RoomDb): List<NoteEntity> {
        val removeList = mutableListOf<NoteEntity>()

        val existNoteList = roomDb.noteDao.get(bin = false)
        val existRollNoteList = existNoteList.filter { it.type == NoteType.ROLL }

        for (item in model.noteList) {
            when (item.type) {
                NoteType.TEXT -> if (needSkipTextNote(item, existNoteList)) removeList.add(item)
                NoteType.ROLL -> {
                    val itemRollList = model.rollList.filter { it.noteId == item.id }
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
    fun clearList(removeNoteList: List<NoteEntity>, model: Model) {
        for (item in removeNoteList) {
            if (item.type == NoteType.ROLL) {
                model.rollList.removeAll { it.noteId == item.id }
                model.rollVisibleList.removeAll { it.noteId == item.id }
            }

            model.rankList.filter { it.noteId.contains(item.id) }.forEach {
                it.noteId.remove(item.id)
            }

            model.alarmList.removeAll { it.noteId == item.id }
        }

        model.noteList.removeAll(removeNoteList)
    }

    /**
     * Return list for remove with items from [Model.rankList], which already exists in [roomDb].
     *
     * Also update [NoteEntity.rankId] and [NoteEntity.rankPs] (if need) for
     * items in [Model.noteList].
     */
    @RunPrivate
    suspend fun clearRankList(model: Model, roomDb: RoomDb) {
        val removeList = mutableListOf<RankEntity>()
        val existRankList = roomDb.rankDao.get()

        for (item in model.rankList) {
            val index = existRankList.indexOfFirst { it.name == item.name }
            val existItem = existRankList.getOrNull(index) ?: continue

            removeList.add(item)

            model.noteList.filter { it.rankId == item.id }.forEach {
                it.rankId = existItem.id
                it.rankPs = index
            }
        }

        model.rankList.removeAll(removeList)
    }

    /**
     * Return list for remove with items from [Model.alarmList], which already past.
     *
     * Also change time of [Model.alarmList] items, if user have same date in [roomDb].
     */
    @RunPrivate
    suspend fun clearAlarmList(model: Model, roomDb: RoomDb) {
        val removeList = mutableListOf<AlarmEntity>()
        val notificationList = roomDb.alarmDao.getList()

        for (item in model.alarmList) {
            val calendar = item.date.getCalendarOrNull() ?: continue

            if (calendar.beforeNow()) {
                removeList.add(item)
                continue
            }

            while (notificationList.any { it.alarm.date == item.date }) {
                item.date = calendar.apply { add(Calendar.MINUTE, 1) }.getText()
            }
        }

        model.alarmList.removeAll(removeList)
    }

    /**
     * Insert notes from [Model.noteList] to [roomDb].
     *
     * Also update (if need) noteId for items in other lists.
     *
     * Need reset [NoteEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertNoteList(model: Model, roomDb: RoomDb) {
        model.noteList.forEach { item ->
            val oldId = item.id

            item.id = roomDb.noteDao.insert(item.apply { id = Note.Default.ID })

            model.rollList.filter { it.noteId == oldId }.forEach { it.noteId = item.id }
            model.rollVisibleList.filter { it.noteId == oldId }.forEach { it.noteId = item.id }

            model.rankList.filter { it.noteId.contains(oldId) }.forEach {
                it.noteId.remove(oldId)
                it.noteId.add(item.id)
            }

            model.alarmList.filter { it.noteId == oldId }.forEach { it.noteId = item.id }
        }
    }

    /**
     * Insert roll items from [Model.rollList] to [roomDb].
     *
     * Need reset [RollEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollList(model: Model, roomDb: RoomDb) {
        model.rollList.forEach { roomDb.rollDao.insert(it.apply { id = Roll.Default.ID }) }
    }

    /**
     * Insert rollVisible items from [Model.rollVisibleList] to [roomDb].
     *
     * Need reset [RollVisibleEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollVisibleList(model: Model, roomDb: RoomDb) {
        model.rollVisibleList.forEach {
            roomDb.rollVisibleDao.insert(it.apply { id = RollVisible.Default.ID })
        }
    }

    /**
     * Insert rank items from [Model.rankList] to [roomDb].
     *
     * Also update (if need) rankId and position for items from [Model.noteList].
     *
     * Need reset [RankEntity.id] for prevent unique id exception.
     * And update [RankEntity.position].
     */
    @RunPrivate
    suspend fun insertRankList(model: Model, roomDb: RoomDb) {
        val existRankList = roomDb.rankDao.get().toMutableList()

        model.rankList.forEach { item ->
            val oldId = item.id
            item.id = roomDb.rankDao.insert(item.apply {
                id = Rank.Default.ID
                position = existRankList.size
            })

            existRankList.add(item)

            val updateList = model.noteList.filter { it.rankId == oldId }
            updateList.forEach {
                it.rankId = item.id
                it.rankPs = item.position
            }
            roomDb.noteDao.update(updateList)
        }
    }

    /**
     * Insert alarm items from [Model.noteList] to [roomDb].
     *
     * Need reset [AlarmEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertAlarmList(model: Model, roomDb: RoomDb) {
        model.alarmList.forEach { roomDb.alarmDao.insert(it.apply { id = Alarm.Default.ID }) }
    }

}