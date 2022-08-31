package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.NoteRepoImpl
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Interface for communicate with [NoteRepoImpl]
 */
interface NoteRepo {

    suspend fun getCount(isBin: Boolean): Int

    suspend fun getList(
        sort: Sort,
        isBin: Boolean,
        isOptimal: Boolean,
        filterVisible: Boolean
    ): MutableList<NoteItem>

    suspend fun getItem(id: Long, isOptimal: Boolean): NoteItem?

    suspend fun getRollList(noteId: Long): MutableList<RollItem>


    suspend fun isListHide(): Boolean

    suspend fun clearBin()


    suspend fun deleteNote(item: NoteItem)

    suspend fun restoreNote(item: NoteItem)

    suspend fun clearNote(item: NoteItem)

    // Repo save and update functions

    /**
     * TODO #THINK in notes list need add fast convert
     * (prepare all data - update note - suspend work with db)
     */
    suspend fun convertNote(item: NoteItem.Text): NoteItem.Roll

    /**
     * TODO #THINK in notes list need add fast convert
     * (prepare all data - update note - suspend work with db)
     */
    suspend fun convertNote(item: NoteItem.Roll, useCache: Boolean): NoteItem.Text

    suspend fun getCopyText(item: NoteItem): String

    suspend fun saveNote(item: NoteItem.Text, isCreate: Boolean)

    suspend fun saveNote(item: NoteItem.Roll, isCreate: Boolean)

    suspend fun updateRollCheck(item: NoteItem.Roll, p: Int)

    suspend fun updateRollCheck(item: NoteItem.Roll, isCheck: Boolean)

    suspend fun updateNote(item: NoteItem)

    suspend fun setRollVisible(item: NoteItem.Roll)

    // Repo backup functions

    suspend fun getNoteBackup(): List<NoteEntity>

    suspend fun getRollBackup(noteIdList: List<Long>): List<RollEntity>

    suspend fun getRollVisibleBackup(noteIdList: List<Long>): List<RollVisibleEntity>

}