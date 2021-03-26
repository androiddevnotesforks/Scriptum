package sgtmelon.scriptum.presentation.screen.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.extension.InsetsDir
import sgtmelon.scriptum.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.extension.updatePadding
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity

/**
 * Parent class for preference fragments.
 */
abstract class ParentPreferenceFragment : PreferenceFragmentCompat() {

    protected val activity get() = getActivity() as? AppActivity
    protected val fm get() = parentFragmentManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupInsets()
    }

    private fun setupInsets() {
        listView.clipToPadding = false
        listView.doOnApplyWindowInsets { view, insets, _, padding, _ ->
            view.updatePadding(InsetsDir.BOTTOM, insets, padding)
            return@doOnApplyWindowInsets insets
        }
    }
}