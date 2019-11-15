package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
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

    fun openTextNoteBind(noteItem: NoteItem, func: TextNoteScreen.() -> Unit = {}) = apply {
        TextNoteScreen.invoke(func, State.READ, noteItem)
    }

    fun openRollNoteBind(noteItem: NoteItem, func: RollNoteScreen.() -> Unit = {}) = apply {
        RollNoteScreen.invoke(func, State.READ, noteItem)
    }

    fun openAlarm(noteItem: NoteItem, dateList: List<String>? = null,
                  func: AlarmScreen.() -> Unit = {}) = apply {
        AlarmScreen.invoke(func, noteItem, dateList)
    }

    fun openNotification(empty: Boolean = false,
                         func: NotificationScreen.() -> Unit = {}) = apply {
        NotificationScreen.invoke(func, empty)
    }


    companion object {
        operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply(func)
    }

}