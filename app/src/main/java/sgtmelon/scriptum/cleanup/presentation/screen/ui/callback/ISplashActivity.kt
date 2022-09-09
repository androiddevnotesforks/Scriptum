package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback

import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel

/**
 * Interface for communication [ISplashViewModel] with [SplashActivity].
 */
interface ISplashActivity : SystemReceiver.Bridge.TidyUp,
    SystemReceiver.Bridge.Bind {

    fun openIntroScreen()

    fun openMainScreen()

    fun openAlarmScreen(id: Long)

    fun openNoteScreen(id: Long, color: Int, type: Int)

    fun openNotificationScreen()

    fun openHelpDisappearScreen()

    fun openCreateNoteScreen(type: NoteType)
}