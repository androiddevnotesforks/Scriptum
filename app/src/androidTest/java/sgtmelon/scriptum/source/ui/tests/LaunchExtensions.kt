package sgtmelon.scriptum.source.ui.tests

import androidx.test.core.app.launchActivity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceScreen
import sgtmelon.scriptum.source.ui.model.key.NoteState
import sgtmelon.scriptum.source.ui.screen.alarm.AlarmScreen
import sgtmelon.scriptum.source.ui.screen.main.BinScreen
import sgtmelon.scriptum.source.ui.screen.main.MainScreen
import sgtmelon.scriptum.source.ui.screen.main.NotesScreen
import sgtmelon.scriptum.source.ui.screen.main.RankScreen
import sgtmelon.scriptum.source.ui.screen.note.NoteScreen
import sgtmelon.scriptum.source.ui.screen.notifications.NotificationsScreen
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceScreen
import sgtmelon.scriptum.source.ui.screen.preference.backup.BackupPreferenceScreen
import sgtmelon.scriptum.source.ui.screen.preference.menu.MenuPreferenceScreen
import sgtmelon.scriptum.source.ui.screen.preference.notes.NotesPreferenceScreen

inline fun ParentUiTest.launchMain(
    before: () -> Unit = {},
    after: MainScreen.() -> Unit
) {
    before()
    launchActivity<MainActivity>(Screens.toMain(context))
    MainScreen(after)
}

inline fun ParentUiTest.launchHelpMain(
    before: () -> Unit = {},
    after: MainScreen.() -> Unit
) {
    before()
    launchActivity<MainActivity>(Screens.toMain(context))

    /**
     * Skip screen assertion because dialog will be shown very fast and there is no time for
     * assert main screen.
     */
    MainScreen().apply(after)
}

//region Rank functions

inline fun ParentUiTest.launchRank(isEmpty: Boolean = false, func: RankScreen.() -> Unit = {}) {
    launchMain { openRank(isEmpty, func) }
}

inline fun ParentUiTest.launchRankList(
    count: Int = 15,
    crossinline func: RankScreen.(list: MutableList<RankItem>) -> Unit = {}
) {
    val list = db.fillRank(count)
    launchRank { func(list) }
}

inline fun ParentUiTest.launchRankItem(
    item: RankItem,
    crossinline func: RankScreen.(RankItem) -> Unit
) {
    launchRank { func(item) }
}

//endregion

//region Notes functions

inline fun ParentUiTest.launchNotes(
    isEmpty: Boolean = false,
    isHide: Boolean = false,
    func: NotesScreen.() -> Unit = {}
) {
    launchMain { openNotes(isEmpty, isHide, func) }
}

inline fun ParentUiTest.launchNotesList(
    count: Int = 15,
    crossinline func: NotesScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillNotes(count)
    launchNotes { func(list) }
}

inline fun <T : NoteItem> ParentUiTest.launchNotesItem(
    item: T,
    crossinline func: NotesScreen.(T) -> Unit
) {
    launchNotes { func(item) }
}

//endregion

//region Bin functions

inline fun ParentUiTest.launchBin(
    isEmpty: Boolean = false,
    func: BinScreen.() -> Unit = {}
) {
    launchMain { openBin(isEmpty, func) }
}

inline fun ParentUiTest.launchBinList(
    count: Int = 15,
    crossinline func: BinScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillBin(count)
    launchBin { func(list) }
}

inline fun <T : NoteItem> ParentUiTest.launchBinItem(
    item: T,
    crossinline func: BinScreen.(T) -> Unit
) {
    launchBin { func(item) }
}

//endregion

//region Note functions

inline fun ParentUiTest.launchTextNote(
    before: () -> Unit = {},
    item: NoteItem.Text,
    isRankEmpty: Boolean = true,
    after: TextNoteScreen.() -> Unit
) {
    before()
    val intent = Screens.Note.toExist(context, item)
    launchActivity<NoteActivity>(intent)
    val state = if (item.isBin) NoteState.BIN else NoteState.READ
    NoteScreen().openText(after, state, item, isRankEmpty)
}

inline fun ParentUiTest.launchNewTextNote(
    before: () -> Unit = {},
    item: NoteItem.Text,
    isRankEmpty: Boolean = true,
    after: TextNoteScreen.() -> Unit
) {
    before()
    val intent = Screens.Note.toNew(context, item)
    launchActivity<NoteActivity>(intent)
    NoteScreen().openText(after, NoteState.NEW, item, isRankEmpty)
}

inline fun ParentUiTest.launchRollNote(
    before: () -> Unit = {},
    item: NoteItem.Roll,
    isRankEmpty: Boolean = true,
    after: RollNoteScreen.() -> Unit
) {
    before()
    val intent = Screens.Note.toExist(context, item)
    launchActivity<NoteActivity>(intent)
    val state = if (item.isBin) NoteState.BIN else NoteState.READ
    NoteScreen().openRoll(after, state, item, isRankEmpty)
}

inline fun ParentUiTest.launchNewRollNote(
    before: () -> Unit = {},
    item: NoteItem.Roll,
    isRankEmpty: Boolean = true,
    after: RollNoteScreen.() -> Unit
) {
    before()
    val intent = Screens.Note.toNew(context, item)
    launchActivity<NoteActivity>(intent)
    NoteScreen().openRoll(after, NoteState.NEW, item, isRankEmpty)
}

//endregion

//region Preference functions

inline fun ParentUiTest.launchMenuPreference(
    before: () -> Unit = {},
    after: MenuPreferenceScreen.() -> Unit
) {
    before()
    val intent = Screens.toPreference(context, PreferenceScreen.MENU)
    launchActivity<PreferenceActivity>(intent)
    MenuPreferenceScreen(after)
}

inline fun ParentUiTest.launchBackupPreference(
    before: () -> Unit = {},
    after: BackupPreferenceScreen.() -> Unit
) {
    before()
    val intent = Screens.toPreference(context, PreferenceScreen.BACKUP)
    launchActivity<PreferenceActivity>(intent)
    BackupPreferenceScreen(after)
}

inline fun ParentUiTest.launchNotesPreference(
    before: () -> Unit = {},
    after: NotesPreferenceScreen.() -> Unit
) {
    before()
    val intent = Screens.toPreference(context, PreferenceScreen.NOTES)
    launchActivity<PreferenceActivity>(intent)
    NotesPreferenceScreen(after)
}

inline fun ParentUiTest.launchAlarmPreference(
    before: () -> Unit = {},
    after: AlarmPreferenceScreen.() -> Unit
) {
    before()
    val intent = Screens.toPreference(context, PreferenceScreen.ALARM)
    launchActivity<PreferenceActivity>(intent)
    AlarmPreferenceScreen(after)
}

//endregion

//region Notifications functions

inline fun ParentUiTest.launchNotifications(
    before: () -> Unit = {},
    isEmpty: Boolean = false,
    after: NotificationsScreen.() -> Unit = {}
) {
    before()
    launchActivity<NotificationsActivity>(Screens.toNotifications(context))
    NotificationsScreen(after, isEmpty)
}

inline fun ParentUiTest.launchNotificationsList(
    count: Int = 15,
    crossinline func: NotificationsScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillNotifications(count)
    launchNotifications { func(list) }
}

inline fun <T : NoteItem> ParentUiTest.launchNotificationsItem(
    item: T,
    crossinline func: NotificationsScreen.(T) -> Unit
) {
    db.insertNotification(item)
    launchNotifications { func(item) }
}

//endregion

//region Alarm functions

inline fun <T : NoteItem> ParentUiTest.launchAlarm(
    item: T,
    crossinline func: AlarmScreen.(T) -> Unit
) {
    launchSplashAlarm(item) { alarmScreen(item) { func(item) } }
}

/**
 * [AlarmScreen] must be closed after calling of [func], and this extension will check it.
 */
inline fun <T : NoteItem> ParentUiTest.launchAlarmClose(
    item: T,
    crossinline func: AlarmScreen.(T) -> Unit
) {
    launchSplashAlarm(item) {
        alarmScreen(item) { func(item) }
        mainScreen { openNotes { assertItem(item) } }
    }
}

//endregion