package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : ParentPreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_develop, rootKey)
    }
}