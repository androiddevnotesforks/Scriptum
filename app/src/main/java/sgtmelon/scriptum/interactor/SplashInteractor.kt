package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interactor for [SplashViewModel]
 */
class SplashInteractor(context: Context) :
        ParentInteractor(context),
        ISplashInteractor {

    override val firstStart: Boolean get() = iPreferenceRepo.firstStart

}