package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.ISplashViewModel

/**
 * Interface for communication [ISplashViewModel] with [SplashActivity].
 */
interface ISplashActivity {

    fun openIntroScreen()

    fun openMainScreen()

    fun openAlarmScreen(id: Long)

    fun openNoteScreen(id: Long, @Color color: Int, type: Int)

    fun openNotificationScreen()

    fun openHelpDisappearScreen()

}