package sgtmelon.scriptum.interactor

import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.presentation.screen.vm.SplashViewModel

/**
 * Interactor for [SplashViewModel].
 */
class SplashInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        ISplashInteractor {

    override val firstStart: Boolean get() = preferenceRepo.firstStart

}