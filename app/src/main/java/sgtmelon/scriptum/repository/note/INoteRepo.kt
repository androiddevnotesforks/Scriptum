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


    fun convertToRoll(noteItem: NoteItem)

    fun convertToText(noteItem: NoteItem)

    suspend fun getCopyText(noteItem: NoteItem): String

    fun saveTextNote(noteItem: NoteItem, isCreate: Boolean)

    fun saveRollNote(noteItem: NoteItem, isCreate: Boolean)


    fun updateRollCheck(noteItem: NoteItem, p: Int)

    fun updateRollCheck(noteItem: NoteItem)

    fun updateNote(noteItem: NoteItem)

}