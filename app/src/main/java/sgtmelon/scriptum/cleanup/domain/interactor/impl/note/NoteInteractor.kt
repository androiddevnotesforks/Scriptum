package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel

/**
 * Interactor for [INoteViewModel].
 */
class NoteInteractor(private val preferenceRepo: AppPreferences) : ParentInteractor(),
        INoteInteractor {

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor
}