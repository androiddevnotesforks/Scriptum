package sgtmelon.scriptum.cleanup.domain.interactor.impl

import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel

/**
 * Interactor for [ISplashViewModel].
 */
class SplashInteractor(private val preferenceRepo: Preferences) : ParentInteractor(),
        ISplashInteractor {

    override val isFirstStart: Boolean get() = preferenceRepo.isFirstStart

}