package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.test.prod.RunPrivate

/**
 * Repository for work with ranks.
 */
class RankRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val rankDataSource: RankDataSource,
    private val alarmDataSource: AlarmDataSource,
    private val converter: RankConverter
) : RankRepo {

    override suspend fun getCount(): Int = rankDataSource.getCount()

    override suspend fun getList(): List<RankItem> {
        val list = converter.toItem(rankDataSource.getList())

        for (item in list) {
            item.bindCount = noteDataSource.getBindCount(item.noteId)
            item.notificationCount = alarmDataSource.getCount(item.noteId)
        }

        return list
    }

    /**
     * Return list of rank id's which is visible.
     */
    override suspend fun getIdVisibleList(): List<Long> = rankDataSource.getIdVisibleList()


    override suspend fun insert(name: String): RankItem? {
        val id = rankDataSource.insert(RankEntity[name]) ?: return null
        return RankItem(id, name = name)
    }

    override suspend fun insert(item: RankItem) {
        /** Correcting of notes rankId/Ps. */
        for (id in item.noteId) {
            val noteEntity = noteDataSource.get(id) ?: continue
            noteEntity.rankId = item.id
            noteEntity.rankPs = item.position

            noteDataSource.update(noteEntity)
        }

        /** Id after insert will be the same, like in [item]. */
        rankDataSource.insert(converter.toEntity(item))
    }

    override suspend fun delete(item: RankItem) {
        for (id in item.noteId) {
            /** Remove rank data from note. */
            val noteEntity = noteDataSource.get(id) ?: continue
            noteEntity.rankId = Note.Default.RANK_ID
            noteEntity.rankPs = Note.Default.RANK_PS

            noteDataSource.update(noteEntity)
        }

        rankDataSource.delete(item)
    }

    override suspend fun update(item: RankItem) = rankDataSource.update(converter.toEntity(item))

    override suspend fun updatePositions(list: List<RankItem>, noteIdList: List<Long>) {
        updateRankPositionsForNotes(list, noteIdList)
        rankDataSource.update(converter.toEntity(list))
    }

    /**
     * Update [NoteEntity.rankPs] for notes from [noteIdList] which related with [list].
     */
    @RunPrivate
    suspend fun updateRankPositionsForNotes(list: List<RankItem>, noteIdList: List<Long>) {
        if (noteIdList.isEmpty()) return

        val noteList = noteDataSource.getList(noteIdList)
        for (entity in noteList) {
            entity.rankPs = list.firstOrNull { it.id == entity.rankId }?.position ?: continue
        }

        noteDataSource.update(noteList)
    }


    /**
     * Add [NoteEntity.id] to [RankEntity.noteId] or remove after some changes.
     */
    override suspend fun updateConnection(item: NoteItem) {
        val list = rankDataSource.getList()
        val checkArray = calculateCheckArray(list, item.rankId)

        rankDataSource.update(updateNoteId(list, checkArray, item.id))
    }

    @RunPrivate
    fun calculateCheckArray(rankList: List<RankEntity>, rankId: Long): BooleanArray {
        return BooleanArray(rankList.size).apply {
            rankList.indexOfFirst { it.id == rankId }.takeIf { it != -1 }?.let {
                set(it, true)
            }
        }
    }

    @RunPrivate
    fun updateNoteId(
        list: List<RankEntity>,
        checkArray: BooleanArray,
        noteId: Long
    ): List<RankEntity> {
        if (list.size != checkArray.size) return list

        for ((i, item) in list.withIndex()) {
            val checkValue = checkArray.getOrNull(i) ?: continue

            when {
                checkValue && !item.noteId.contains(noteId) -> item.noteId.add(noteId)
                !checkValue -> item.noteId.remove(noteId)
            }
        }

        return list
    }


    /**
     * Return array with all rank names.
     */
    override suspend fun getDialogItemArray(emptyName: String): Array<String> {
        return ArrayList<String>().apply {
            add(emptyName)
            addAll(rankDataSource.getNameList())
        }.toTypedArray()
    }

    /**
     * Return rank id by [position].
     */
    override suspend fun getId(position: Int): Long {
        return if (position == Note.Default.RANK_PS) {
            Note.Default.RANK_ID
        } else {
            rankDataSource.getId(position) ?: Note.Default.RANK_ID
        }
    }
}