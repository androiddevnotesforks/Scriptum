package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory

/**
 * Parent class for fragments.
 */
abstract class ParentFragment : Fragment() {

    protected val fm get() = parentFragmentManager

    private lateinit var delegatorFactory: DelegatorFactory
    protected val delegators get() = delegatorFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegatorFactory = DelegatorFactory(view.context, lifecycle)
    }
}