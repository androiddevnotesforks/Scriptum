package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [HelpPrefFragment].
 */
class HelpPrefActivity : ParentPreferenceActivity(
    R.layout.activity_preference_help,
    R.id.help_pref_parent_container,
    R.id.help_pref_fragment_container,
    R.string.pref_title_other_help
) {

    override val tag: String = FragmentFactory.Preference.Tag.HELP

    override val fragment by lazy { fragmentFactory.getHelpFragment() }

    companion object {
        operator fun get(context: Context) = Intent(context, HelpPrefActivity::class.java)
    }
}