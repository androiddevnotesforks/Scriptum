package sgtmelon.scriptum.app.ui.splash

import android.content.Intent

/**
 * Интерфейс для общения [SplashViewModel] и [SplashActivity]
 */
interface SplashCallback {

    fun startFromNotification(arrayIntent: Array<Intent>)

    fun startNormal(intent: Intent)

}