package sgtmelon.scriptum.repository.note

import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem

/**
 * Interface for communicate with [NoteRepo]
 */
interface INoteRepo {

    fun getList(@Sort sort: Int, bin: Boolean, optimal: Boolean,
                filterVisible: Boolean): MutableList<NoteItem>

    fun getItem(id: Long, optimisation: Boolean): NoteItem?

    fun getRollList(noteId: Long): MutableList<RollItem>


    fun isListHide(): Boolean

    suspend fun clearBin()


    suspend fun deleteNote(noteItem: NoteItem)

    suspend fun restoreNote(noteItem: NoteItem)

    suspend fun clearNote(noteItem: NoteItem)


    suspend fun convertToRoll(noteItem: NoteItem)

    suspend fun convertToText(noteItem: NoteItem)

    suspend fun getCopyText(noteItem: NoteItem): String

    suspend fun saveTextNote(noteItem: NoteItem, isCreate: Boolean)

    suspend fun saveRollNote(noteItem: NoteItem, isCreate: Boolean)


    suspend fun updateRollCheck(noteItem: NoteItem, p: Int)

    suspend fun updateRollCheck(noteItem: NoteItem, check: Boolean)

    suspend fun updateNote(noteItem: NoteItem)

}