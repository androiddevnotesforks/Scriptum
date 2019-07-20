package sgtmelon.scriptum.screen.ui.callback

import android.content.Intent

import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Интерфейс для общения [SplashViewModel] с [SplashActivity]
 *
 * @author SerjantArbuz
 */
interface ISplashActivity {

    fun startActivities(arrayIntent: Array<Intent>)

    fun startActivity(intent: Intent)

}