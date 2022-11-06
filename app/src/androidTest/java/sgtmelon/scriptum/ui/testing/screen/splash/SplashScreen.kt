package sgtmelon.scriptum.ui.testing.screen.splash

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.screen.AlarmScreen
import sgtmelon.scriptum.cleanup.ui.screen.NotificationsScreen
import sgtmelon.scriptum.cleanup.ui.screen.main.MainScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.preference.help.HelpDisappearScreen
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.ui.testing.screen.parent.UiPart

/**
 * Class for control Intent launches inside [SplashActivity].
 */
class SplashScreen : UiPart() {

    inline fun mainScreen(func: MainScreen.() -> Unit = {}) = MainScreen(func)

    inline fun bindNoteScreen(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        TextNoteScreen(func, State.READ, item, isRankEmpty)
    }

    inline fun bindNoteScreen(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        RollNoteScreen(func, State.READ, item, isRankEmpty)
    }

    inline fun alarmScreen(
        item: NoteItem,
        dateList: List<String>? = null,
        func: AlarmScreen.() -> Unit = {}
    ) = apply {
        AlarmScreen(func, item, dateList)
    }

    inline fun notificationsScreen(
        isEmpty: Boolean = true,
        func: NotificationsScreen.() -> Unit = {}
    ) = apply {
        NotificationsScreen(func, isEmpty)
    }

    inline fun helpDisappearScreen(func: HelpDisappearScreen.() -> Unit = {}) = apply {
        HelpDisappearScreen(func)
    }

    inline fun createNoteScreen(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        TextNoteScreen(func, State.NEW, item, isRankEmpty)
    }

    inline fun createNoteScreen(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        RollNoteScreen(func, State.NEW, item, isRankEmpty)
    }

    companion object {
        inline operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply(func)
    }
}