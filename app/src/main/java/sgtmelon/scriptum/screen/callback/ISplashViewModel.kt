package sgtmelon.scriptum.screen.callback

import android.os.Bundle
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Интерфейс для общения [SplashActivity] с [SplashViewModel]
 *
 * @author SerjantArbuz
 */
interface ISplashViewModel : IParentViewModel {

    fun onSetup(bundle: Bundle?)

}