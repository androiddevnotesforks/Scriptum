package sgtmelon.scriptum.infrastructure.screen.splash

import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Interface for communication [ISplashViewModel] with [SplashActivity].
 */
interface ISplashActivity : SystemReceiver.Bridge.TidyUp,
    SystemReceiver.Bridge.Bind {

    fun openIntroScreen()

    fun openMainScreen()

    fun openAlarmScreen(noteId: Long)

    fun openNoteScreen(noteId: Long, color: Int, type: Int)

    fun openNotificationScreen()

    fun openHelpDisappearScreen()

    fun openNewNoteScreen(type: NoteType)
}