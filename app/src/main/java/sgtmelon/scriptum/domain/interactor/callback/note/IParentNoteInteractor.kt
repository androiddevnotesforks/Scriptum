package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.presentation.control.note.save.SaveControl

/**
 * Parent interface for [ITextNoteInteractor] and [IRollNoteInteractor].
 */
interface IParentNoteInteractor : IParentInteractor {

    fun getSaveModel(): SaveControl.Model

}