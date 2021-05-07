package sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [DevelopFragment].
 */
class DevelopActivity : ParentPreferenceActivity(
    R.layout.activity_preference_develop,
    R.id.develop_parent_container,
    R.id.develop_fragment_container,
    R.string.pref_title_other_develop
) {

    override val tag: String = FragmentFactory.Preference.Tag.DEVELOP

    override val fragment by lazy { fragmentFactory.getDevelopFragment() }

    companion object {
        operator fun get(context: Context) = Intent(context, DevelopActivity::class.java)
    }
}