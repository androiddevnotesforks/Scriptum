package sgtmelon.scriptum.ui.testing.parent

import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.screen.SplashScreen
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * This function not PROTECTED because used inside launch extensions in other classes for
 * short test launch.
 */
inline fun ParentUiTest.launch(
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    before()
    testRule.launchActivity(Intent())
    SplashScreen(after)
}

inline fun ParentUiTest.launchAlarm(
    item: NoteItem,
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    before()
    testRule.launchActivity(InstanceFactory.Splash.getAlarm(context, item.id))
    SplashScreen(after)
}

inline fun ParentUiTest.launchBind(
    item: NoteItem,
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    before()
    testRule.launchActivity(InstanceFactory.Splash.getBind(context, item))
    SplashScreen(after)
}

inline fun ParentUiTest.launchNotifications(
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    before()
    testRule.launchActivity(InstanceFactory.Splash.getNotification(context))
    SplashScreen(after)
}

inline fun ParentUiTest.launchHelpDisappear(
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    before()
    testRule.launchActivity(InstanceFactory.Splash.getHelpDisappear(context))
    SplashScreen(after)
}

inline fun ParentUiTest.launchNewNote(
    type: NoteType,
    before: () -> Unit = {},
    after: SplashScreen.() -> Unit
) {
    before()
    testRule.launchActivity(InstanceFactory.Splash.getNewNote(context, type))
    SplashScreen(after)
}
