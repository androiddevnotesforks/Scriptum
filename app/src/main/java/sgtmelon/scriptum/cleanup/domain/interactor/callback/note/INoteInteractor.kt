package sgtmelon.scriptum.cleanup.domain.interactor.callback.note

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.NoteInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel

/**
 * Interface for communication [INoteViewModel] with [NoteInteractor].
 */
interface INoteInteractor : IParentInteractor {

    @Color val defaultColor: Int
}