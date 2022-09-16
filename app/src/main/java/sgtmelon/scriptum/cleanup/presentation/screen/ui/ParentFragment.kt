package sgtmelon.scriptum.cleanup.presentation.screen.ui

import androidx.fragment.app.Fragment
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator

/**
 * Parent class for fragments.
 */
abstract class ParentFragment : Fragment() {

    protected val fm get() = parentFragmentManager

    protected val toast = ToastDelegator(lifecycle)

}