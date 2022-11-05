package sgtmelon.scriptum.ui.testing.parent

import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.screen.SplashScreen
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

inline fun ParentUiTest.launch(
    before: () -> Unit,
    intent: Intent,
    after: SplashScreen.() -> Unit
) {
    before()
    testRule.launchActivity(intent)
    SplashScreen(after)
}

inline fun ParentUiTest.launch(before: () -> Unit = {}, after: SplashScreen.() -> Unit) {
    launch(before, Intent(), after)
}

inline fun ParentUiTest.launchAlarm(
    item: NoteItem,
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    launch(before, InstanceFactory.Splash.getAlarm(context, item.id), after)
}

inline fun ParentUiTest.launchBind(
    item: NoteItem,
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    launch(before, InstanceFactory.Splash.getBind(context, item), after)
}

inline fun ParentUiTest.launchNotifications(
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    launch(before, InstanceFactory.Splash.getNotification(context), after)
}

inline fun ParentUiTest.launchHelpDisappear(
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    launch(before, InstanceFactory.Splash.getHelpDisappear(context), after)
}

inline fun ParentUiTest.launchNewNote(
    type: NoteType,
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    launch(before, InstanceFactory.Splash.getNewNote(context, type), after)
}