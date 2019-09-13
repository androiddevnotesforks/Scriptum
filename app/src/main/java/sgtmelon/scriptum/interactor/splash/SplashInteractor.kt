package sgtmelon.scriptum.interactor.splash

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interactor for [SplashViewModel]
 *
 * @author SerjantArbuz
 */
class SplashInteractor(context: Context): ParentInteractor(context), ISplashInteractor {

    override val firstStart: Boolean
        get() = iPreferenceRepo.firstStart.apply {
            if (this) iPreferenceRepo.firstStart = false
        }

}