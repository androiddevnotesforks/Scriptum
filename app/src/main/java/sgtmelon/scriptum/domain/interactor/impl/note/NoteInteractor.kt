package sgtmelon.scriptum.domain.interactor.impl.note

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.vm.callback.note.INoteViewModel

/**
 * Interactor for [INoteViewModel].
 */
class NoteInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        INoteInteractor {

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor
}