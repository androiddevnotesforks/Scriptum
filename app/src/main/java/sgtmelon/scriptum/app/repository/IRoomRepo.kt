package sgtmelon.scriptum.app.repository

import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.office.annot.def.CheckDef

/**
 * Интерфейс для общения с [RoomRepo]
 */
interface IRoomRepo {

    fun getNoteRepoList(fromBin: Boolean): MutableList<NoteRepo>

    fun clearBin()

    fun restoreNoteItem(id: Long)

    fun clearNoteItem(id: Long)

    /**
     *
     */


    /**
     *
     */


    /**
     *
     */

    fun updateNoteItemCheck(noteItem: NoteItem, @CheckDef check: Int)

    fun updateNoteItemBind(id: Long, status: Boolean)

    fun convertNoteItem(noteRepo: NoteRepo)

    fun deleteNoteItem(id: Long)

}