package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.updatePadding
import sgtmelon.scriptum.cleanup.presentation.control.toast.ToastControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.infrastructure.widgets.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class ParentPreferenceFragment : PreferenceFragmentCompat() {

    protected val activity get() = getActivity() as? AppActivity
    protected val fm get() = parentFragmentManager

    val toastControl by lazy { ToastControl(context) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toastControl.initLazy()

        setupRecycler()
        setupInsets()
    }

    private fun setupRecycler() {
        listView.clipToPadding = false
        listView.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
    }

    private fun setupInsets() {
        listView.doOnApplyWindowInsets { view, insets, _, padding, _ ->
            view.updatePadding(InsetsDir.BOTTOM, insets, padding)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toastControl.onDestroy()
    }
}