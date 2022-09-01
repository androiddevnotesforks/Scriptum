package sgtmelon.scriptum.cleanup.data.repository.room

import java.util.Calendar
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.utils.beforeNow
import sgtmelon.common.utils.getCalendarOrNull
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Repository for work with backup data.
 */
class BackupRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val rollDataSource: RollDataSource,
    private val rollVisibleDataSource: RollVisibleDataSource,
    private val rankDataSource: RankDataSource,
    private val alarmDataSource: AlarmDataSource
) : BackupRepo {

    override suspend fun insertData(model: Model, isSkipImports: Boolean): ImportResult {
        val startSize = model.noteList.size

        if (isSkipImports) clearList(getRemoveNoteList(model), model)

        clearRankList(model)
        clearAlarmList(model)

        insertNoteList(model)
        insertRollList(model)
        insertRollVisibleList(model)
        insertRankList(model)
        insertAlarmList(model)

        return if (isSkipImports) {
            ImportResult.Skip(skipCount = startSize - model.noteList.size)
        } else {
            ImportResult.Simple
        }
    }


    /**
     * Return list for remove with items from [Model.noteList], which
     * already exists in [Database].
     */
    @RunPrivate
    suspend fun getRemoveNoteList(model: Model): List<NoteEntity> {
        val removeList = mutableListOf<NoteEntity>()

        val existNoteList = noteDataSource.getList(isBin = false)
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
                    if (needSkipRollNote(item, itemRollList, existRollNoteList)) {
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
        existNoteList: List<NoteEntity>
    ): Boolean {
        for (existItem in existNoteList.filter { it.name == item.name }) {
            val existRollList = rollDataSource.getList(existItem.id)

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
     * already exists in [Database].
     *
     * Also update [NoteEntity.rankId] and [NoteEntity.rankPs] (if need) for
     * items in [Model.noteList].
     */
    @RunPrivate
    suspend fun clearRankList(model: Model) {
        val removeList = mutableListOf<RankEntity>()
        val existRankList = rankDataSource.getList()

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
     * Also change time of [Model.alarmList] items, if user have same date in [Database].
     */
    @RunPrivate
    suspend fun clearAlarmList(model: Model) {
        val removeList = mutableListOf<AlarmEntity>()
        val notificationList = alarmDataSource.getItemList()

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
     * Insert notes from [Model.noteList] to [Database].
     *
     * Also update (if need) noteId for items in other lists.
     *
     * Need reset [NoteEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertNoteList(model: Model) {
        /**
         * Need for prevent overriding already updated items.
         * Because new noteId may be equals oldNoteId for next item.
         */
        val skipRollIdList = mutableListOf<Long>()
        val skipVisibleIdList = mutableListOf<Long>()
        val skipAlarmIdList = mutableListOf<Long>()

        for (item in model.noteList) {
            val oldNoteId = item.id

            /** Catch of insert errors happen inside dataSource. */
            item.id = noteDataSource.insert(item.copy(id = Note.Default.ID)) ?: continue

            updateRollLink(oldNoteId, item.id, model.rollList, skipRollIdList)
            updateRollVisibleLink(oldNoteId, item.id, model.rollVisibleList, skipVisibleIdList)
            updateRankLink(oldNoteId, item.id, model.rankList)
            updateAlarmList(oldNoteId, item.id, model.alarmList, skipAlarmIdList)
        }
    }

    @RunPrivate
    fun updateRollLink(
        oldNoteId: Long,
        newNoteId: Long,
        rollList: List<RollEntity>,
        skipIdList: MutableList<Long>
    ) {
        val list = rollList.filter { !skipIdList.contains(it.id) && it.noteId == oldNoteId }

        for (it in list) {
            val rollId = it.id
            if (rollId != null) {
                skipIdList.add(rollId)
            }

            it.noteId = newNoteId
        }
    }

    @RunPrivate
    fun updateRollVisibleLink(
        oldNoteId: Long,
        newNoteId: Long,
        rollVisibleList: List<RollVisibleEntity>,
        skipIdList: MutableList<Long>
    ) {
        val list = rollVisibleList.filter { !skipIdList.contains(it.id) && it.noteId == oldNoteId }

        for (it in list) {
            skipIdList.add(it.id)
            it.noteId = newNoteId
        }
    }

    /**
     * Note may be connected only to one rank (or not connected at all).
     */
    @RunPrivate
    fun updateRankLink(oldNoteId: Long, newNoteId: Long, rankList: List<RankEntity>) {
        val entity = rankList.firstOrNull {
            it.id == newNoteId && it.noteId.contains(oldNoteId)
        } ?: return

        entity.noteId.remove(oldNoteId)
        entity.noteId.add(newNoteId)
    }

    @RunPrivate
    fun updateAlarmList(
        oldNoteId: Long,
        newNoteId: Long,
        alarmList: List<AlarmEntity>,
        skipIdList: MutableList<Long>
    ) {
        val list = alarmList.filter { !skipIdList.contains(it.id) && it.noteId == oldNoteId }

        for (it in list) {
            skipIdList.add(it.id)
            it.noteId = newNoteId
        }
    }

    /**
     * Insert roll items from [Model.rollList] to [Database].
     *
     * Need reset [RollEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollList(model: Model) {
        for (it in model.rollList) {
            it.id = rollDataSource.insert(it.copy(id = Roll.Default.ID))
        }
    }

    /**
     * Insert rollVisible items from [Model.rollVisibleList] to [Database].
     *
     * Need reset [RollVisibleEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollVisibleList(model: Model) {
        for (it in model.rollVisibleList) {
            /** Catch of insert errors happen inside dataSource. */
            it.id = rollVisibleDataSource.insert(it.copy(id = RollVisible.Default.ID)) ?: continue
        }
    }

    /**
     * Insert rank items from [Model.rankList] to [Database].
     *
     * Also update (if need) rankId and position for items from [Model.noteList].
     *
     * Need reset [RankEntity.id] for prevent unique id exception.
     * And update [RankEntity.position].
     */
    // TODO if existRankList contain same name as inside [model.rankList]. Need add check for this.
    @RunPrivate
    suspend fun insertRankList(model: Model) {
        val existRankList = rankDataSource.getList().toMutableList()

        /**
         * Need for prevent overriding already updated notes.
         * Because new rankId may be equals oldId for next item.
         */
        val skipIdList = mutableListOf<Long>()

        for (item in model.rankList) {
            val oldId = item.id

            /** Catch of insert errors happen inside dataSource. */
            item.id = rankDataSource.insert(
                item.copy(
                    id = Rank.Default.ID, position = existRankList.size
                )
            ) ?: continue

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
            noteDataSource.update(model.noteList)
        }
    }

    /**
     * Insert alarm items from [Model.noteList] to [Database].
     *
     * Need reset [AlarmEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertAlarmList(model: Model) {
        for (it in model.alarmList) {
            /** Catch of insert errors happen inside dataSource. */
            it.id = alarmDataSource.insert(it.copy(id = Alarm.Default.ID)) ?: continue
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