package sgtmelon.scriptum.domain.interactor.impl.note

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.vm.impl.note.NoteViewModel

/**
 * Interactor for [NoteViewModel].
 */
class NoteInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        INoteInteractor {

    @Theme override val theme: Int get() = preferenceRepo.theme

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor

}