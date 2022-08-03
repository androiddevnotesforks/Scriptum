package sgtmelon.scriptum.cleanup.ui.screen

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.screen.main.MainScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [SplashActivity].
 */
class SplashScreen : ParentUi() {

    fun introScreen(func: IntroScreen.() -> Unit = {}) = IntroScreen(func)

    fun mainScreen(func: MainScreen.() -> Unit = {}) = MainScreen(func)

    fun openTextNoteBind(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        TextNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun openRollNoteBind(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        RollNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun openAlarm(
        item: NoteItem,
        dateList: List<String>? = null,
        func: AlarmScreen.() -> Unit = {}
    ) = apply {
        AlarmScreen(func, item, dateList)
    }

    fun openNotification(
        isEmpty: Boolean = false,
        func: NotificationScreen.() -> Unit = {}
    ) = apply {
        NotificationScreen(func, isEmpty)
    }


    companion object {
        operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply(func)
    }
}