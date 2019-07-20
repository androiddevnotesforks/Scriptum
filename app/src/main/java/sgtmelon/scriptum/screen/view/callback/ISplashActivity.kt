package sgtmelon.scriptum.screen.view.callback

import android.content.Intent

import sgtmelon.scriptum.screen.view.SplashActivity
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