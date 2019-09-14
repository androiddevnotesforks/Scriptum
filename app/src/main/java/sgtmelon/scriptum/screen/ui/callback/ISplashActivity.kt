package sgtmelon.scriptum.screen.ui.callback

import android.content.Intent
import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashActivity]
 */
interface ISplashActivity : AlarmCallback.Cancel {

    fun startActivities(arrayIntent: Array<Intent>)

    fun startActivity(intent: Intent)

}