package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Класс для ui контроля экрана [SplashActivity]
 *
 * @author SerjantArbuz
 */
class SplashScreen : ParentUi() {

    fun introScreen(func: IntroScreen.() -> Unit = {}) = IntroScreen().apply {
        assert { onDisplayContent() }
        func()
    }

    fun mainScreen(func: MainScreen.() -> Unit = {}) = MainScreen().apply {
        assert { onDisplayContent() }
        func()
    }

    fun openTextNoteNotification(func: TextNoteScreen.() -> Unit = {}) = TextNoteScreen().apply {
        assert { onDisplayContent(State.READ) }
        func()
    }

    fun openRollNoteNotification(func: RollNoteScreen.() -> Unit = {}) = RollNoteScreen().apply {
        assert { onDisplayContent(State.READ) }
        func()
    }

    companion object {
        operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply { func() }
    }

}