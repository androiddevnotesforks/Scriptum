package sgtmelon.scriptum.cleanup.data.repository.room

import java.util.Calendar
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.toCalendarOrNull
import sgtmelon.extensions.toText
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
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.test.prod.RunPrivate

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

    override suspend fun getData(): ParserResult.Export {
        val noteList = noteDataSource.getList(isBin = false)

        val noteIdList = noteList.filter { it.type == NoteType.ROLL }.map { it.id }

        val rollList = rollDataSource.getList(noteIdList)
        val rollVisibleList = rollVisibleDataSource.getList(noteIdList)
        val rankList = rankDataSource.getList()
        val alarmList = alarmDataSource.getList(noteIdList)

        return ParserResult.Export(noteList, rollList, rollVisibleList, rankList, alarmList)
    }

    //region Insert function

    override suspend fun insertData(
        result: ParserResult.Import,
        isSkipImports: Boolean
    ): ImportResult {
        val startSize = result.noteList.size

        if (isSkipImports) clearList(getRemoveNoteList(result), result)

        clearRankList(result)
        clearAlarmList(result)

        insertNoteList(result)
        insertRollList(result)
        insertRollVisibleList(result)
        insertRankList(result)
        insertAlarmList(result)

        return if (isSkipImports) {
            ImportResult.Skip(skipCount = startSize - result.noteList.size)
        } else {
            ImportResult.Simple
        }
    }

    /**
     * Return list for remove with items from [ParserResult.Import.noteList], which
     * already exists in [Database].
     */
    @RunPrivate
    suspend fun getRemoveNoteList(result: ParserResult.Import): List<NoteEntity> {
        val removeList = mutableListOf<NoteEntity>()

        val existNoteList = noteDataSource.getList(isBin = false)
        val existRollNoteList = existNoteList.filter { it.type == NoteType.ROLL }

        for (item in result.noteList) {
            when (item.type) {
                NoteType.TEXT -> {
                    if (needSkipTextNote(item, existNoteList)) {
                        removeList.add(item)
                    }
                }
                NoteType.ROLL -> {
                    val itemRollList = result.rollList.filter { it.noteId == item.id }
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
    fun clearList(removeNoteList: List<NoteEntity>, result: ParserResult.Import) {
        for (item in removeNoteList) {
            if (item.type == NoteType.ROLL) {
                result.rollList.removeAll { it.noteId == item.id }
                result.rollVisibleList.removeAll { it.noteId == item.id }
            }

            for (it in result.rankList.filter { it.noteId.contains(item.id) }) {
                it.noteId.remove(item.id)
            }

            result.alarmList.removeAll { it.noteId == item.id }
        }

        result.noteList.removeAll(removeNoteList)
    }

    /**
     * Remove entities from [ParserResult.Import.rankList], which already exists in [Database]
     * (unique name).
     *
     * And update [NoteEntity.rankId]/[NoteEntity.rankPs] (if needed) for
     * items in [ParserResult.Import.noteList].
     */
    @RunPrivate
    suspend fun clearRankList(result: ParserResult.Import) {
        val removeList = mutableListOf<RankEntity>()
        val existRankList = rankDataSource.getList()

        for (entity in result.rankList) {
            val index = existRankList.indexOfFirst {
                entity.name.uppercase() == it.name.uppercase()
            }
            val existEntity = existRankList.getOrNull(index) ?: continue

            removeList.add(entity)

            for (it in result.noteList.filter { it.rankId == entity.id }) {
                it.rankId = existEntity.id
                it.rankPs = index
            }
        }

        result.rankList.removeAll(removeList)
    }

    /**
     * Return list for remove with items from [ParserResult.Import.alarmList], which
     * already past.
     *
     * Also change time of [ParserResult.Import.alarmList] items, if user have same date in [Database].
     */
    @RunPrivate
    suspend fun clearAlarmList(result: ParserResult.Import) {
        val removeList = mutableListOf<AlarmEntity>()
        val notificationList = alarmDataSource.getItemList()

        for (item in result.alarmList) {
            val calendar = item.date.toCalendarOrNull() ?: continue

            if (calendar.isBeforeNow()) {
                removeList.add(item)
                continue
            }

            moveNotificationTime(item, calendar, notificationList)
        }

        result.alarmList.removeAll(removeList)
    }

    // TODO common staff check : ShiftDateIfExistUseCase
    @RunPrivate
    fun moveNotificationTime(
        item: AlarmEntity,
        calendar: Calendar,
        list: List<NotificationItem>
    ) {
        while (list.any { it.alarm.date == item.date }) {
            item.date = calendar.apply { add(Calendar.MINUTE, 1) }.toText()
        }
    }


    /**
     * Insert notes from [ParserResult.Import.noteList] to [Database].
     *
     * Also update (if need) noteId for items in other lists.
     *
     * Need reset [NoteEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertNoteList(result: ParserResult.Import) {
        val existRankList = rankDataSource.getList()

        /**
         * Need for prevent overriding already updated items.
         * Because new noteId may be equals oldNoteId for next item.
         */
        val skipRollIdList = mutableListOf<Long>()
        val skipVisibleIdList = mutableListOf<Long>()
        val skipAlarmIdList = mutableListOf<Long>()

        for (item in result.noteList) {
            val oldNoteId = item.id

            /** Catch of insert errors happen inside dataSource. */
            item.id = noteDataSource.insert(item.copy(id = Note.Default.ID)) ?: continue

            updateRollLink(oldNoteId, item.id, result.rollList, skipRollIdList)
            updateRollVisibleLink(oldNoteId, item.id, result.rollVisibleList, skipVisibleIdList)
            updateRankLink(oldNoteId, item, result.rankList, existRankList)
            updateAlarmList(oldNoteId, item.id, result.alarmList, skipAlarmIdList)
        }

        rankDataSource.update(existRankList)
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
     *
     * [existRankList] - for searching connection in existing (inside database) rank.
     *
     * We clean up [rankList] inside [clearRankList] (if some ranks already exists). There we
     * update connection note-rank by updating [NoteEntity.rankId]. Here we can simply find
     * needed [RankEntity] inside [existRankList] by this id connection.
     */
    @RunPrivate
    fun updateRankLink(
        oldNoteId: Long,
        noteEntity: NoteEntity,
        rankList: List<RankEntity>,
        existRankList: List<RankEntity>
    ) {
        /**
         * - [NoteEntity] connected to [RankEntity] via [NoteEntity.rankId]
         * - [RankEntity] connected to [NoteEntity] via [RankEntity.noteId] list
         */
        val rankEntity = rankList.firstOrNull {
            noteEntity.rankId == it.id && it.noteId.contains(oldNoteId)
        }

        if (rankEntity != null) {
            rankEntity.noteId.remove(oldNoteId)
            rankEntity.noteId.add(noteEntity.id)
        } else {
            val existRankEntity = existRankList.firstOrNull { it.id == noteEntity.rankId }
            existRankEntity?.noteId?.add(noteEntity.id)
        }
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
     * Insert roll items from [ParserResult.Import.rollList] to [Database].
     *
     * Need reset [RollEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollList(result: ParserResult.Import) {
        for (it in result.rollList) {
            it.id = rollDataSource.insert(it.copy(id = Roll.Default.ID))
        }
    }

    /**
     * Insert rollVisible items from [ParserResult.Import.rollVisibleList] to [Database].
     *
     * Need reset [RollVisibleEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertRollVisibleList(result: ParserResult.Import) {
        for (it in result.rollVisibleList) {
            /** Catch of insert errors happen inside dataSource. */
            it.id = rollVisibleDataSource.insert(it.copy(id = RollVisible.Default.ID)) ?: continue
        }
    }

    /**
     * Insert rank items from [ParserResult.Import.rankList] to [Database].
     *
     * Also update (if need) rankId and position for items from [ParserResult.Import.noteList].
     *
     * Need reset [RankEntity.id] for prevent unique id exception.
     * And update [RankEntity.position].
     */
    @RunPrivate
    suspend fun insertRankList(result: ParserResult.Import) {
        val existRankList = rankDataSource.getList().toMutableList()

        /**
         * Need for prevent overriding already updated notes.
         * Because new rankId may be equals oldId for next item.
         */
        val skipIdList = mutableListOf<Long>()

        for (item in result.rankList) {
            val oldId = item.id

            /** Catch of insert errors happen inside dataSource. */
            item.id = rankDataSource.insert(
                item.copy(id = Rank.Default.ID, position = existRankList.size)
            ) ?: continue

            existRankList.add(item)

            for (it in result.noteList.filter {
                !skipIdList.contains(it.id) && it.rankId == oldId
            }) {
                skipIdList.add(it.id)
                it.rankId = item.id
                it.rankPs = item.position
            }
        }

        if (skipIdList.isNotEmpty()) {
            noteDataSource.update(result.noteList)
        }
    }

    /**
     * Insert alarm items from [ParserResult.Import.noteList] to [Database].
     *
     * Need reset [AlarmEntity.id] for prevent unique id exception.
     */
    @RunPrivate
    suspend fun insertAlarmList(result: ParserResult.Import) {
        for (it in result.alarmList) {
            /** Catch of insert errors happen inside dataSource. */
            it.id = alarmDataSource.insert(it.copy(id = Alarm.Default.ID)) ?: continue
        }
    }

    //endregion
}