package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.setPaddingInsets
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.widgets.listeners.RecyclerOverScrollListener

/**
 * Parent class for preference fragments.
 */
abstract class ParentPreferenceFragment : PreferenceFragmentCompat() {

    // TODO сделать доступ к open через интерфейс для фрагментов main экрана
    // TODO добавить open для родительского фрагмента
    // TODO проверить save/respore для open

    protected val fm get() = parentFragmentManager

    private lateinit var delegatorFactory: DelegatorFactory
    protected val delegators get() = delegatorFactory

    protected val open: OpenState = OpenState(lifecycle)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegatorFactory = DelegatorFactory(view.context, lifecycle)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        open.restore(savedInstanceState)

        setupRecycler()
        setupInsets()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        open.save(outState)
    }

    private fun setupRecycler() {
        listView.clipToPadding = false
        listView.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
    }

    private fun setupInsets() {
        listView.setPaddingInsets(InsetsDir.BOTTOM)
    }
}