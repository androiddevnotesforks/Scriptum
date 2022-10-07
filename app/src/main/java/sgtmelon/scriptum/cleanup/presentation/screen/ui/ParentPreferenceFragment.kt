package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.setPaddingInsets
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.widgets.listeners.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class ParentPreferenceFragment : PreferenceFragmentCompat() {

    protected val fm get() = parentFragmentManager

    private lateinit var delegatorFactory: DelegatorFactory
    protected val delegators get() = delegatorFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegatorFactory = DelegatorFactory(view.context, lifecycle)
    }

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