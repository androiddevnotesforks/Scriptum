package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.NoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.vm.impl.note.NoteViewModel

/**
 * Interface for communication [NoteViewModel] with [NoteInteractor]
 */
interface INoteInteractor : IParentInteractor {

    @Theme val theme: Int?

    @Color val defaultColor: Int?

}