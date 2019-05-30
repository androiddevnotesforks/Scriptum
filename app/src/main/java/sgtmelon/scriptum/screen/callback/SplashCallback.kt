package sgtmelon.scriptum.screen.callback

import android.content.Intent

import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Интерфейс для общения [SplashViewModel] и [SplashActivity]
 *
 * @author SerjantArbuz
 */
interface SplashCallback {

    fun startActivities(arrayIntent: Array<Intent>)

    fun startActivity(intent: Intent)

}