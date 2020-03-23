package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashActivity]
 */
interface ISplashActivity {

    fun startIntroActivity()

    fun startMainActivity()

    fun startAlarmActivity(id: Long)

    fun startNoteActivity(id: Long, @Color color: Int, type: Int)

    fun startNotificationActivity()

}