package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [SplashActivity].
 */
class SplashScreen : ParentUi() {

    fun introScreen(func: IntroScreen.() -> Unit = {}) = IntroScreen.invoke(func)

    fun mainScreen(func: MainScreen.() -> Unit = {}) = MainScreen.invoke(func)

    fun openTextNoteBind(noteModel: NoteModel, func: TextNoteScreen.() -> Unit = {}) = apply {
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRollNoteBind(noteModel: NoteModel, func: RollNoteScreen.() -> Unit = {}) = apply {
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openAlarm(noteModel: NoteModel, dateList: List<String>? = null,
                  func: AlarmScreen.() -> Unit = {}) = apply {
        AlarmScreen.invoke(func, noteModel, dateList)
    }


    companion object {
        operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply(func)
    }

}