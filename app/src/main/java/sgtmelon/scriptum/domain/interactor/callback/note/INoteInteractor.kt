package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.NoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.vm.callback.note.INoteViewModel

/**
 * Interface for communication [INoteViewModel] with [NoteInteractor].
 */
interface INoteInteractor : IParentInteractor {

    @Color val defaultColor: Int
}