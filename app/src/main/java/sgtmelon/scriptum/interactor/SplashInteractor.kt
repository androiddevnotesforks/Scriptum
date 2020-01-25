package sgtmelon.scriptum.interactor

import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interactor for [SplashViewModel].
 */
class SplashInteractor(private val iPreferenceRepo: IPreferenceRepo) : ParentInteractor(),
        ISplashInteractor {

    override val firstStart: Boolean get() = iPreferenceRepo.firstStart

}