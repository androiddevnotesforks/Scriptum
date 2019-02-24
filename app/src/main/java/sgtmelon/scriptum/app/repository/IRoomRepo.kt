package sgtmelon.scriptum.app.repository

import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem

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

    fun getRankVisibleList(): List<Long>

    fun getNoteRepo(id: Long): NoteRepo

    fun getRankDialogName(): Array<String>

    /**
     *
     */


    /**
     *
     */

    fun updateNoteItemCheck(noteItem: NoteItem, check: Boolean)

    fun updateNoteItemBind(id: Long, status: Boolean)

    fun convertNoteItem(noteRepo: NoteRepo)

    fun deleteNoteItem(id: Long)

}