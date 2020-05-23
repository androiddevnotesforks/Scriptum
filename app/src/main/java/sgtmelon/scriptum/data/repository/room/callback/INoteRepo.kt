package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.NoteRepo
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem

/**
 * Interface for communicate with [NoteRepo]
 */
interface INoteRepo {

    suspend fun getCount(bin: Boolean): Int

    suspend fun getList(@Sort sort: Int, bin: Boolean, isOptimal: Boolean,
                        filterVisible: Boolean): MutableList<NoteItem>

    suspend fun getItem(id: Long, isOptimal: Boolean): NoteItem?

    suspend fun getRollList(noteId: Long): MutableList<RollItem>


    suspend fun isListHide(): Boolean

    suspend fun clearBin()


    suspend fun deleteNote(noteItem: NoteItem)

    suspend fun restoreNote(noteItem: NoteItem)

    suspend fun clearNote(noteItem: NoteItem)


    /**
     * TODO #THINK in notes list need add fast convert
     * (prepare all data - update note - suspend work with db)
     */
    suspend fun convertNote(noteItem: NoteItem.Text): NoteItem.Roll

    /**
     * TODO #THINK in notes list need add fast convert
     * (prepare all data - update note - suspend work with db)
     */
    suspend fun convertNote(noteItem: NoteItem.Roll, useCache: Boolean): NoteItem.Text

    suspend fun getCopyText(noteItem: NoteItem): String

    suspend fun saveNote(noteItem: NoteItem.Text, isCreate: Boolean)

    suspend fun saveNote(noteItem: NoteItem.Roll, isCreate: Boolean)


    suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int)

    suspend fun updateRollCheck(noteItem: NoteItem.Roll, check: Boolean)

    suspend fun updateNote(noteItem: NoteItem)


    suspend fun setRollVisible(noteId: Long, isVisible: Boolean)

    suspend fun getRollVisible(noteId: Long): Boolean

}