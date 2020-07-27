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
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import java.util.*

/**
 * Repository of [RoomDb] which work with backup data.
 */
class BackupRepo(override val roomProvider: RoomProvider) : IBackupRepo,
        IRoomWork {

    override suspend fun insertData(model: Model, importSkip: Boolean): ImportResult {
        return takeFromRoom {
            val startSize = model.noteList.size

            if (importSkip) clearList(getRemoveNoteList(model, roomDb = this), model)

            clearRankList(model, roomDb = this)
            clearAlarmList(model, roomDb = this)

            insertNoteList(model, roomDb = this)
            insertRollList(model, roomDb = this)
            insertRollVisibleList(model, roomDb = this)
            insertRankList(model, roomDb = this)
            insertAlarmList(model, roomDb = this)

            return@takeFromRoom if (importSkip) {
                ImportResult.Skip(skipCount = startSize - model.noteList.size)
            } else {
                ImportResult.Simple
            }
        }
    }


    /**
     * Return list for remove with items from [Model.noteList], which
     * already exists in [roomDb].
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
    suspend fun needSkipRollNote(
            item: NoteEntity,
            rollList: List<RollEntity>,
            existNoteList: List<NoteEntity>,
            roomDb: RoomDb
    ): Boolean {
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
     * Return list for remove with items from [Model.rankList], which
     * already exists in [roomDb].
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
     * Return list for remove with items from [Model.alarmList], which
     * already past.
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

            moveNotificationTime(item, calendar, notificationList)
        }

        model.alarmList.removeAll(removeList)
    }

    @RunPrivate
    fun moveNotificationTime(
            item: AlarmEntity,
            calendar: Calendar,
            list: List<NotificationItem>
    ) {
        while (list.any { it.alarm.date == item.date }) {
            item.date = calendar.apply { add(Calendar.MINUTE, 1) }.getText()
        }
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
        /**
         * Need for prevent overriding already updated items.
         * Because new noteId may be equals oldId for next item.
         */
        val skipRollIdList = mutableListOf<Long>()
        val skipRollVisibleIdList = mutableListOf<Long>()
        val skipAlarmList = mutableListOf<Long>()

        model.noteList.forEach { item ->
            val oldId = item.id

            item.id = roomDb.noteDao.insert(item.apply { id = Note.Default.ID })

            model.rollList.filter {
                !skipRollIdList.contains(it.id) && it.noteId == oldId
            }.forEach {
                it.id?.let { id -> skipRollIdList.add(id) }
                it.noteId = item.id
            }

            model.rollVisibleList.filter {
                !skipRollVisibleIdList.contains(it.id) && it.noteId == oldId
            }.forEach {
                skipRollVisibleIdList.add(it.id)
                it.noteId = item.id
            }

            /**
             * Note may be connected only to one rank (or not connected at all).
             */
            model.rankList.firstOrNull {
                it.id == item.rankId && it.noteId.contains(oldId)
            }?.apply {
                noteId.remove(oldId)
                noteId.add(item.id)
            }

            model.alarmList.filter {
                !skipAlarmList.contains(it.id) && it.noteId == oldId
            }.forEach {
                skipAlarmList.add(it.id)
                it.noteId = item.id
            }
        }
    }

    /**
     * Insert roll items from [Model.rollList] to [roomDb].
     *
     * Need reset [RollEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollList(model: Model, roomDb: RoomDb) {
        model.rollList.forEach {
            it.id = roomDb.rollDao.insert(it.apply { id = Roll.Default.ID })
        }
    }

    /**
     * Insert rollVisible items from [Model.rollVisibleList] to [roomDb].
     *
     * Need reset [RollVisibleEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollVisibleList(model: Model, roomDb: RoomDb) {
        model.rollVisibleList.forEach {
            it.id = roomDb.rollVisibleDao.insert(it.apply { id = RollVisible.Default.ID })
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

        /**
         * Need for prevent overriding already updated notes.
         * Because new rankId may be equals oldId for next item.
         */
        val skipIdList = mutableListOf<Long>()

        model.rankList.forEach { item ->
            val oldId = item.id

            item.id = roomDb.rankDao.insert(item.apply {
                id = Rank.Default.ID
                position = existRankList.size
            })

            existRankList.add(item)

            model.noteList.filter { !skipIdList.contains(it.id) && it.rankId == oldId }.forEach {
                skipIdList.add(it.id)
                it.rankId = item.id
                it.rankPs = item.position
            }
        }

        if (skipIdList.isNotEmpty()) {
            roomDb.noteDao.update(model.noteList)
        }
    }

    /**
     * Insert alarm items from [Model.noteList] to [roomDb].
     *
     * Need reset [AlarmEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertAlarmList(model: Model, roomDb: RoomDb) {
        model.alarmList.forEach {
            it.id = roomDb.alarmDao.insert(it.apply { id = Alarm.Default.ID })
        }
    }

    data class Model(
            val noteList: MutableList<NoteEntity>,
            val rollList: MutableList<RollEntity>,
            val rollVisibleList: MutableList<RollVisibleEntity>,
            val rankList: MutableList<RankEntity>,
            val alarmList: MutableList<AlarmEntity>
    ) {
        companion object {
            operator fun get(parserResult: ParserResult): Model = with(parserResult) {
                return Model(
                    noteList.toMutableList(),
                    rollList.toMutableList(),
                    rollVisibleList.toMutableList(),
                    rankList.toMutableList(),
                    alarmList.toMutableList()
                )
            }
        }
    }

}