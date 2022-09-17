package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.setPaddingInsets
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.scriptum.infrastructure.widgets.listeners.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class ParentPreferenceFragment : PreferenceFragmentCompat() {

    protected val activity get() = getActivity() as? AppActivity
    protected val fm get() = parentFragmentManager

    protected val toast = ToastDelegator(lifecycle)

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecycler()
        setupInsets()
    }

    private fun setupRecycler() {
        listView.clipToPadding = false
        listView.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
    }

    private fun setupInsets() {
        listView.setPaddingInsets(InsetsDir.BOTTOM)
    }
}