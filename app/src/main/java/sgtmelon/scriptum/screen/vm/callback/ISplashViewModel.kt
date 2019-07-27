package sgtmelon.scriptum.screen.vm.callback

import android.os.Bundle
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interface for communication [SplashActivity] with [SplashViewModel]
 *
 * @author SerjantArbuz
 */
interface ISplashViewModel : IParentViewModel {

    fun onSetup(bundle: Bundle?)

}