package sgtmelon.scriptum.presentation.screen.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.presentation.control.toast.ToastControl

/**
 * Parent class for fragments.
 */
abstract class ParentFragment : Fragment() {

    protected val fm get() = parentFragmentManager

    protected val toastControl by lazy { ToastControl(context) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toastControl.initLazy()
    }

    override fun onDestroy() {
        super.onDestroy()
        toastControl.onDestroy()
    }
}