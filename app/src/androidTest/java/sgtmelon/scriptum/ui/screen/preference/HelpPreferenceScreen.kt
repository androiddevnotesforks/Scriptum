package sgtmelon.scriptum.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpPreferenceFragment
import sgtmelon.scriptum.ui.logic.preference.HelpPreferenceLogic

/**
 * Class for UI control of [HelpPreferenceFragment].
 */
class HelpPreferenceScreen :
    ParentPreferenceScreen<HelpPreferenceLogic>(R.string.pref_title_other_help) {

    override val screenLogic = HelpPreferenceLogic()

    companion object {
        operator fun invoke(func: HelpPreferenceScreen.() -> Unit): HelpPreferenceScreen {
            return HelpPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}