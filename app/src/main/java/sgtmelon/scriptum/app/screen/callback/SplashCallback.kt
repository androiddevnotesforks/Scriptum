package sgtmelon.scriptum.app.screen.callback

import android.content.Intent

import sgtmelon.scriptum.app.screen.view.SplashActivity
import sgtmelon.scriptum.app.screen.vm.SplashViewModel

/**
 * Интерфейс для общения [SplashViewModel] и [SplashActivity]
 *
 * @author SerjantArbuz
 */
interface SplashCallback {

    fun startFromNotification(arrayIntent: Array<Intent>)

    fun startNormal(intent: Intent)

}