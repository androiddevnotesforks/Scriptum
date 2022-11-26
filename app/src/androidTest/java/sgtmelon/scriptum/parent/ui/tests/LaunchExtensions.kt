package sgtmelon.scriptum.parent.ui.tests

import androidx.test.core.app.launchActivity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.parent.ui.screen.main.MainScreen
import sgtmelon.scriptum.parent.ui.screen.main.NotesScreen
import sgtmelon.scriptum.parent.ui.screen.main.RankScreen
import sgtmelon.scriptum.parent.ui.screen.preference.MenuPreferenceScreen
import sgtmelon.scriptum.parent.ui.screen.preference.alarm.AlarmPreferenceScreen

inline fun ParentUiTest.launchMain(
    before: () -> Unit = {},
    after: MainScreen.() -> Unit
) {
    before()
    launchActivity<MainActivity>(InstanceFactory.Main[context])
    MainScreen(after)
}

//region Launch Rank functions

inline fun ParentUiTest.launchRank(isEmpty: Boolean = false, func: RankScreen.() -> Unit = {}) {
    launchMain { openRank(isEmpty, func) }
}

inline fun ParentUiTest.launchRankList(
    count: Int = 15,
    crossinline func: RankScreen.(list: MutableList<RankItem>) -> Unit = {}
) {
    val list = db.fillRank(count)
    launchMain { openRank { func(list) } }
}

inline fun ParentUiTest.launchRankItem(
    item: RankItem,
    crossinline func: RankScreen.(RankItem) -> Unit
) {
    launchMain { openRank { func(item) } }
}

//endregion

//region Launch Notes functions

inline fun ParentUiTest.launchNotesList(
    count: Int = 15,
    crossinline func: NotesScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillNotes(count)
    launchSplash { mainScreen { openNotes { func(list) } } }
}

inline fun <T : NoteItem> ParentUiTest.launchNotesItem(
    item: T,
    crossinline func: NotesScreen.(T) -> Unit
) {
    launchSplash { mainScreen { openNotes { func(item) } } }
}

//endregion

//region Launch Preference functions

inline fun ParentUiTest.launchMenuPreference(
    before: () -> Unit = {},
    after: MenuPreferenceScreen.() -> Unit
) {
    before()
    val intent = InstanceFactory.Preference[context, PreferenceScreen.MENU]
    launchActivity<PreferenceActivity>(intent)
    MenuPreferenceScreen(after)
}

inline fun ParentUiTest.launchAlarmPreference(
    before: () -> Unit = {},
    after: AlarmPreferenceScreen.() -> Unit
) {
    before()
    val intent = InstanceFactory.Preference[context, PreferenceScreen.ALARM]
    launchActivity<PreferenceActivity>(intent)
    AlarmPreferenceScreen(after)
}

//endregion
