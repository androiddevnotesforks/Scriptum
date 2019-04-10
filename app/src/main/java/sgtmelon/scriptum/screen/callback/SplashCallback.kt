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

    fun startFromNotification(arrayIntent: Array<Intent>)

    fun startNormal(intent: Intent)

}