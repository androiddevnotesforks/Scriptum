package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [SplashActivity].
 */
class SplashScreen : ParentUi() {

    fun introScreen(func: IntroScreen.() -> Unit = {}) = IntroScreen(func)

    fun mainScreen(func: MainScreen.() -> Unit = {}) = MainScreen(func)

    fun openTextNoteBind(noteItem: NoteItem, isRankEmpty: Boolean = true,
                         func: TextNoteScreen.() -> Unit = {}) = apply {
        TextNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }

    fun openRollNoteBind(noteItem: NoteItem, isRankEmpty: Boolean = true,
                         func: RollNoteScreen.() -> Unit = {}) = apply {
        RollNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }

    fun openAlarm(noteItem: NoteItem, dateList: List<String>? = null,
                  func: AlarmScreen.() -> Unit = {}) = apply {
        AlarmScreen(func, noteItem, dateList)
    }

    fun openNotification(empty: Boolean = false,
                         func: NotificationScreen.() -> Unit = {}) = apply {
        NotificationScreen(func, empty)
    }


    companion object {
        operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply(func)
    }

}