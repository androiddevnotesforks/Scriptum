package sgtmelon.scriptum.cleanup.domain.interactor.impl

import sgtmelon.scriptum.cleanup.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel

/**
 * Interactor for [ISplashViewModel].
 */
class SplashInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        ISplashInteractor {

    override val firstStart: Boolean get() = preferenceRepo.firstStart

}