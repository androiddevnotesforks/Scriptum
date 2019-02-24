package sgtmelon.scriptum.app.repository

import sgtmelon.scriptum.app.model.NoteRepo

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

    fun deleteNoteItem(id: Long)

}