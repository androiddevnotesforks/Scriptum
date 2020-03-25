package sgtmelon.scriptum.domain.interactor

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.presentation.screen.vm.impl.SplashViewModel

/**
 * Interactor for [SplashViewModel].
 */
class SplashInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        ISplashInteractor {

    override val firstStart: Boolean get() = preferenceRepo.firstStart

}