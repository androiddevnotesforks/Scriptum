package sgtmelon.scriptum.screen.ui.callback

import android.content.Intent
import sgtmelon.scriptum.control.alarm.callback.IAlarmBridge
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashActivity]
 */
interface ISplashActivity : IAlarmBridge.Cancel {

    fun startActivities(arrayIntent: Array<Intent>)

    fun startActivity(intent: Intent)

}