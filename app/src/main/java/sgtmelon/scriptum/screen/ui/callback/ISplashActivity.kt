package sgtmelon.scriptum.screen.ui.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashActivity]
 */
interface ISplashActivity {

    fun startIntroActivity()

    fun startMainActivity()

    fun startAlarmActivity(id: Long)

    fun startNoteActivity(id: Long, @Color color: Int, type: NoteType)

    fun startNotificationActivity()

}