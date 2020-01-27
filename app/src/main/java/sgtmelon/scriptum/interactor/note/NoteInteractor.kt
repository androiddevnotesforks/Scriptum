package sgtmelon.scriptum.interactor.note

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interactor for [NoteViewModel].
 */
class NoteInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        INoteInteractor {

    @Theme override val theme: Int get() = preferenceRepo.theme

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor

}