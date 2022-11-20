package sgtmelon.scriptum.cleanup.ui.screen.preference.help

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.logic.preference.HelpPreferenceLogic
import sgtmelon.scriptum.infrastructure.screen.preference.help.HelpPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.ParentPreferencePart

/**
 * Class for UI control of [HelpPreferenceFragment].
 */
class HelpPreferenceScreen :
    ParentPreferencePart<HelpPreferenceLogic>(R.string.pref_title_help) {

    override val screenLogic = HelpPreferenceLogic()

    fun openNotificationDisappear(func: HelpDisappearScreen.() -> Unit = {}) {
        getItem(p = 0).Summary().onItemClick()
        HelpDisappearScreen(func)
    }

    fun openPrivacyPolicy() {
        getItem(p = 2).Simple().onItemClick()
    }

    companion object {
        inline operator fun invoke(func: HelpPreferenceScreen.() -> Unit): HelpPreferenceScreen {
            return HelpPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}