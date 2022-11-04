package sgtmelon.scriptum.cleanup.ui.screen

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.screen.main.MainScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.preference.help.HelpDisappearScreen
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity

/**
 * Class for UI control of [SplashActivity].
 */
class SplashScreen : ParentUi() {

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
        func: NotificationsScreen.() -> Unit = {}
    ) = apply {
        NotificationsScreen(func, isEmpty)
    }

    fun openHelpDisappear(func: HelpDisappearScreen.() -> Unit = {}) = apply {
        HelpDisappearScreen(func)
    }

    fun openCreateText(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        TextNoteScreen(func, State.NEW, item, isRankEmpty)
    }

    fun openCreateRoll(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        RollNoteScreen(func, State.NEW, item, isRankEmpty)
    }

    companion object {
        operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply(func)
    }
}