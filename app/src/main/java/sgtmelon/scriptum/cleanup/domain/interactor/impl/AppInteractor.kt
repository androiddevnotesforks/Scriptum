package sgtmelon.scriptum.cleanup.domain.interactor.impl

import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel

/**
 * Interactor for [IAppViewModel].
 */
class AppInteractor(
    private val preferences: Preferences
) : ParentInteractor(),
    IAppInteractor {

    @Theme override val theme: Int get() = preferences.theme

}