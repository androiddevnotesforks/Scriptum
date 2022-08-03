package sgtmelon.scriptum.cleanup.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.cleanup.ui.logic.preference.DeveloperLogic

/**
 * Class for UI control of [DevelopFragment].
 */
class DeveloperScreen : ParentPreferenceScreen<DeveloperLogic>(R.string.pref_title_other_develop) {

    override val screenLogic = DeveloperLogic()

    companion object {
        operator fun invoke(func: DeveloperScreen.() -> Unit): DeveloperScreen {
            return DeveloperScreen().apply { assert() }.apply(func)
        }
    }
}