package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.test.prod.RunProtected

/**
 * Parent ViewModel.
 *
 * [C] is the interface for communicate with UI. Same like in [IParentViewModel].
 */
abstract class ParentViewModel<C>(private var callbackField: C?) : ViewModel(),
    IParentViewModel {

    @RunProtected val callback: C? get() = callbackField

    override fun onDestroy(func: () -> Unit) {
        func()
        callbackField = null
    }
}