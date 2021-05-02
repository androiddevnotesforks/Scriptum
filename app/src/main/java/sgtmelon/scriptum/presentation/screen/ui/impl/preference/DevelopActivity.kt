package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [DevelopFragment].
 */
class DevelopActivity : ParentPreferenceActivity(
    R.layout.activity_develop,
    R.id.develop_parent_container,
    R.id.develop_fragment_container,
    R.string.title_develop
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(DevelopFragment())
    }

    companion object {
        operator fun get(context: Context) = Intent(context, DevelopActivity::class.java)
    }
}