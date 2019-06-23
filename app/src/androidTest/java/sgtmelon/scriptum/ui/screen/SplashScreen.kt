package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
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

    fun introScreen(func: IntroScreen.() -> Unit = {}) = IntroScreen.invoke(func)

    fun mainScreen(func: MainScreen.() -> Unit = {}) = MainScreen.invoke(func)

    fun openTextNoteBind(noteModel: NoteModel, func: TextNoteScreen.() -> Unit = {}) =
            TextNoteScreen.invoke(func, State.READ, noteModel)

    fun openRollNoteBind(noteModel: NoteModel, func: RollNoteScreen.() -> Unit = {}) =
            RollNoteScreen.invoke(func, State.READ, noteModel)

    fun openAlarm(noteModel: NoteModel, func: AlarmScreen.() -> Unit = {}) =
            AlarmScreen.invoke(func, noteModel)

    companion object {
        operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply { func() }
    }

}