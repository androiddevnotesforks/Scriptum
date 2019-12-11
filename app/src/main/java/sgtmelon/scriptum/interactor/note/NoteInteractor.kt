package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interactor for [NoteViewModel]
 */
class NoteInteractor(context: Context) : ParentInteractor(context), INoteInteractor {

    @Color override val defaultColor: Int get() = iPreferenceRepo.defaultColor

}