package sgtmelon.scriptum.cleanup.presentation.screen

import androidx.lifecycle.ViewModel
import sgtmelon.test.prod.RunProtected

/**
 * Parent ViewModel.
 *
 * [C] is the interface for communicate with UI. Same like in [IParentViewModel].
 */
@Deprecated("Use simple ViewModel")
abstract class ParentViewModel<C>(private var callbackField: C?) : ViewModel(),
    IParentViewModel {

    @Deprecated("Remove callback from viewModels (cause it's not presenter)")
    @RunProtected val callback: C? get() = callbackField

    override fun onDestroy(func: () -> Unit) {
        func()
        callbackField = null
    }
}