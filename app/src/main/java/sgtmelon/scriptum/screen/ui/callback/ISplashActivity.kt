package sgtmelon.scriptum.screen.ui.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interface for communication [SplashViewModel] with [SplashActivity]
 */
interface ISplashActivity : ISplashBridge {

    fun startIntroActivity()

    fun startMainActivity()

    fun startNoteActivity(id: Long, type: NoteType)

    fun startAlarmActivity(id: Long, @Color color: Int)

}