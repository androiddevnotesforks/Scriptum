package sgtmelon.scriptum.interactor.callback.note

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.note.NoteInteractor
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.screen.vm.note.NoteViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Interface for communication [NoteViewModel] with [NoteInteractor]
 */
interface INoteInteractor : IParentInteractor {

    @Color val defaultColor: Int

}