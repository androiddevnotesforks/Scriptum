package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel

/**
 * Interactor for [INoteViewModel].
 */
class NoteInteractor(private val preferenceRepo: Preferences) : ParentInteractor(),
        INoteInteractor {

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor
}