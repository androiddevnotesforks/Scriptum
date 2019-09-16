package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Interface for communication [NotesViewModel] with [NotesInteractor]
 */
interface INotesInteractor : IParentInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NoteModel>

    fun isListHide(): Boolean

    fun updateNote(noteEntity: NoteEntity)

    fun convert(noteModel: NoteModel): NoteModel

    suspend fun deleteNote(noteModel: NoteModel)

}