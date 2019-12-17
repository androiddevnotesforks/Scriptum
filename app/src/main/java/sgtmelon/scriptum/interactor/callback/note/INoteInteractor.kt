package sgtmelon.scriptum.interactor.callback.note

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.note.NoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interface for communication [NoteViewModel] with [NoteInteractor]
 */
interface INoteInteractor : IParentInteractor {

    @Theme val theme: Int

    @Color val defaultColor: Int

}