package sgtmelon.scriptum.parent.ui.tests

import androidx.test.core.app.launchActivity
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.parent.ui.screen.preference.MenuPreferenceScreen


inline fun ParentUiTest.launchMenuPreference(
    before: () -> Unit = {},
    after: MenuPreferenceScreen.() -> Unit
) {
    before()
    val intent = InstanceFactory.Preference[context, PreferenceScreen.MENU]
    launchActivity<PreferenceActivity>(intent)
    MenuPreferenceScreen(after)
}