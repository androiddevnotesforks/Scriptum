package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.NoteRepoImpl
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

/**
 * Interface for communicate with [NoteRepoImpl]
 */
interface NoteRepo {

    suspend fun getBindNoteList(sort: Sort): List<NoteItem>

    suspend fun getBinList(sort: Sort): List<NoteItem>

    suspend fun getNotesList(sort: Sort): Pair<List<NoteItem>, Boolean>

    suspend fun getItem(noteId: Long): NoteItem?

    suspend fun getRollList(noteId: Long): MutableList<RollItem>

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
    suspend fun convertNote(item: NoteItem.Roll): NoteItem.Text

    suspend fun saveNote(item: NoteItem.Text, isCreate: Boolean)

    suspend fun saveNote(item: NoteItem.Roll, isCreate: Boolean)

    suspend fun updateRollCheck(item: NoteItem.Roll, p: Int)

    suspend fun updateNote(item: NoteItem)

    suspend fun insertOrUpdateVisible(item: NoteItem.Roll)

}