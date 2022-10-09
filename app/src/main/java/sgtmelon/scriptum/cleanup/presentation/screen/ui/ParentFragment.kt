package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.parent.ParentActivity

/**
 * Parent class for fragments.
 */
abstract class ParentFragment : Fragment() {

    protected val fm get() = parentFragmentManager

    private lateinit var delegatorFactory: DelegatorFactory
    protected val delegators get() = delegatorFactory

    protected val open: OpenState = OpenState(lifecycle)
    protected val parentOpen: OpenState? get() = (activity as? ParentActivity<*>)?.open

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        open.restore(savedInstanceState)

        delegatorFactory = DelegatorFactory(view.context, lifecycle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        open.save(outState)
    }
}