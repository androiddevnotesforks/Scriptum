package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [PreferenceFragment].
 */
class PreferenceActivity : ParentPreferenceActivity(
    R.layout.activity_preference,
    R.id.preference_parent_container,
    R.id.preference_fragment_container,
    R.string.title_preference
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(PreferenceFragment())
    }

    companion object {
        operator fun get(context: Context) = Intent(context, PreferenceActivity::class.java)
    }
}