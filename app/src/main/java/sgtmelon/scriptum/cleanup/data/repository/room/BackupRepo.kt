package sgtmelon.scriptum.cleanup.data.repository.room

import java.util.Calendar
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.utils.beforeNow
import sgtmelon.common.utils.getCalendarOrNull
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.cleanup.data.room.Database
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.data.room.extension.fromRoom
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult

/**
 * Repository of [Database] which work with backup data.
 */
class BackupRepo(override val roomProvider: RoomProvider) : IBackupRepo,
    IRoomWork {

    override suspend fun insertData(model: Model, isSkipImports: Boolean): ImportResult {
        return fromRoom {
            val startSize = model.noteList.size

            if (isSkipImports) clearList(getRemoveNoteList(model, db = this), model)

            clearRankList(model, db = this)
            clearAlarmList(model, db = this)

            insertNoteList(model, db = this)
            insertRollList(model, db = this)
            insertRollVisibleList(model, db = this)
            insertRankList(model, db = this)
            insertAlarmList(model, db = this)

            return@fromRoom if (isSkipImports) {
                ImportResult.Skip(skipCount = startSize - model.noteList.size)
            } else {
                ImportResult.Simple
            }
        }
    }


    /**
     * Return list for remove with items from [Model.noteList], which
     * already exists in [db].
     */
    @RunPrivate
    suspend fun getRemoveNoteList(model: Model, db: Database): List<NoteEntity> {
        val removeList = mutableListOf<NoteEntity>()

        val existNoteList = db.noteDao.getList(isBin = false)
        val existRollNoteList = existNoteList.filter { it.type == NoteType.ROLL }

        for (item in model.noteList) {
            when (item.type) {
                NoteType.TEXT -> {
                    if (needSkipTextNote(item, existNoteList)) {
                        removeList.add(item)
                    }
                }
                NoteType.ROLL -> {
                    val itemRollList = model.rollList.filter { it.noteId == item.id }
                    if (needSkipRollNote(item, itemRollList, existRollNoteList, db)) {
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
        db: Database
    ): Boolean {
        for (existItem in existNoteList.filter { it.name == item.name }) {
            val existRollList = db.rollDao.getList(existItem.id)

            if (rollList.size != existRollList.size) continue

            if (isContainSameItems(rollList, existRollList)) {
                return true
            }
        }

        return false
    }

    @RunPrivate
    fun isContainSameItems(list: List<RollEntity>, existList: List<RollEntity>): Boolean {
        for (item in list) {
            if (!existList.any { item.text == it.text }) {
                return false
            }
        }

        return true
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

            for (it in model.rankList.filter { it.noteId.contains(item.id) }) {
                it.noteId.remove(item.id)
            }

            model.alarmList.removeAll { it.noteId == item.id }
        }

        model.noteList.removeAll(removeNoteList)
    }

    /**
     * Return list for remove with items from [Model.rankList], which
     * already exists in [db].
     *
     * Also update [NoteEntity.rankId] and [NoteEntity.rankPs] (if need) for
     * items in [Model.noteList].
     */
    @RunPrivate
    suspend fun clearRankList(model: Model, db: Database) {
        val removeList = mutableListOf<RankEntity>()
        val existRankList = db.rankDao.getList()

        for (item in model.rankList) {
            val index = existRankList.indexOfFirst { it.name == item.name }
            val existItem = existRankList.getOrNull(index) ?: continue

            removeList.add(item)

            for (it in model.noteList.filter { it.rankId == item.id }) {
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
     * Also change time of [Model.alarmList] items, if user have same date in [db].
     */
    @RunPrivate
    suspend fun clearAlarmList(model: Model, db: Database) {
        val removeList = mutableListOf<AlarmEntity>()
        val notificationList = db.alarmDao.getItemList()

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
     * Insert notes from [Model.noteList] to [db].
     *
     * Also update (if need) noteId for items in other lists.
     *
     * Need reset [NoteEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertNoteList(model: Model, db: Database) {
        /**
         * Need for prevent overriding already updated items.
         * Because new noteId may be equals oldId for next item.
         */
        val skipRollIdList = mutableListOf<Long>()
        val skipRollVisibleIdList = mutableListOf<Long>()
        val skipAlarmList = mutableListOf<Long>()

        for (item in model.noteList) {
            val oldId = item.id

            item.id = db.noteDao.insert(item.apply { id = Note.Default.ID })

            for (it in model.rollList.filter {
                !skipRollIdList.contains(it.id) && it.noteId == oldId
            }) {
                it.id?.let { id -> skipRollIdList.add(id) }
                it.noteId = item.id
            }

            for (it in model.rollVisibleList.filter {
                !skipRollVisibleIdList.contains(it.id) && it.noteId == oldId
            }) {
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

            for (it in model.alarmList.filter {
                !skipAlarmList.contains(it.id) && it.noteId == oldId
            }) {
                skipAlarmList.add(it.id)
                it.noteId = item.id
            }
        }
    }

    /**
     * Insert roll items from [Model.rollList] to [db].
     *
     * Need reset [RollEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollList(model: Model, db: Database) {
        for (it in model.rollList) {
            it.id = db.rollDao.insert(it.apply { id = Roll.Default.ID })
        }
    }

    /**
     * Insert rollVisible items from [Model.rollVisibleList] to [db].
     *
     * Need reset [RollVisibleEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollVisibleList(model: Model, db: Database) {
        for (it in model.rollVisibleList) {
            it.id = db.rollVisibleDao.insert(it.apply { id = RollVisible.Default.ID })
        }
    }

    /**
     * Insert rank items from [Model.rankList] to [db].
     *
     * Also update (if need) rankId and position for items from [Model.noteList].
     *
     * Need reset [RankEntity.id] for prevent unique id exception.
     * And update [RankEntity.position].
     */
    @RunPrivate
    suspend fun insertRankList(model: Model, db: Database) {
        val existRankList = db.rankDao.getList().toMutableList()

        /**
         * Need for prevent overriding already updated notes.
         * Because new rankId may be equals oldId for next item.
         */
        val skipIdList = mutableListOf<Long>()

        for (item in model.rankList) {
            val oldId = item.id

            item.id = db.rankDao.insert(item.apply {
                id = Rank.Default.ID
                position = existRankList.size
            })

            existRankList.add(item)

            for (it in model.noteList.filter {
                !skipIdList.contains(it.id) && it.rankId == oldId
            }) {
                skipIdList.add(it.id)
                it.rankId = item.id
                it.rankPs = item.position
            }
        }

        if (skipIdList.isNotEmpty()) {
            db.noteDao.update(model.noteList)
        }
    }

    /**
     * Insert alarm items from [Model.noteList] to [db].
     *
     * Need reset [AlarmEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertAlarmList(model: Model, db: Database) {
        for (it in model.alarmList) {
            it.id = db.alarmDao.insert(it.apply { id = Alarm.Default.ID })
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