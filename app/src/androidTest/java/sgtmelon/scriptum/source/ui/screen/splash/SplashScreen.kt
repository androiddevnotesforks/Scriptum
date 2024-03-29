package sgtmelon.scriptum.source.ui.screen.splash

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.source.ui.model.key.NoteState
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.scriptum.source.ui.screen.alarm.AlarmScreen
import sgtmelon.scriptum.source.ui.screen.main.MainScreen
import sgtmelon.scriptum.source.ui.screen.note.NoteScreen
import sgtmelon.scriptum.source.ui.screen.notifications.NotificationsScreen

/**
 * Class for control Intent launches inside [SplashActivity].
 */
class SplashScreen : UiPart() {

    inline fun mainScreen(func: MainScreen.() -> Unit = {}) = MainScreen(func)

    inline fun mainHelpDialog(func: MainScreen.() -> Unit = {}) {
        MainScreen().apply(func)
    }

    inline fun notificationsScreen(
        isEmpty: Boolean = true,
        func: NotificationsScreen.() -> Unit = {}
    ) = apply {
        NotificationsScreen(func, isEmpty)
    }

    inline fun alarmScreen(
        item: NoteItem,
        dateList: List<String>? = null,
        func: AlarmScreen.() -> Unit = {}
    ) = apply {
        AlarmScreen(func, item, dateList)
    }

    inline fun bindNoteScreen(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        NoteScreen().openText(func, NoteState.READ, item, isRankEmpty)
    }

    inline fun bindNoteScreen(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        NoteScreen().openRoll(func, NoteState.READ, item, isRankEmpty)
    }

    inline fun createNoteScreen(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        NoteScreen().openText(func, NoteState.NEW, item, isRankEmpty)
    }

    inline fun createNoteScreen(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        NoteScreen().openRoll(func, NoteState.NEW, item, isRankEmpty)
    }

    companion object {
        inline operator fun invoke(func: SplashScreen.() -> Unit) = SplashScreen().apply(func)
    }
}