package sgtmelon.scriptum.presentation.screen.ui.impl

import androidx.fragment.app.Fragment

/**
 * Parent class for fragments.
 */
abstract class ParentFragment : Fragment() {

    protected val fm get() = parentFragmentManager

}