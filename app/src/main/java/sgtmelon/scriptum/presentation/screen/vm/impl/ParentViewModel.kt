package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.test.annotation.RunProtected
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel

/**
 * Parent ViewModel.
 *
 * [C] is the interface for communicate with UI. Same like in [IParentViewModel].
 */
// TODO check tests - need @RunPRivate or not?
abstract class ParentViewModel<C>(private var callbackField: C?) : ViewModel(),
    IParentViewModel {

    @RunProtected val callback get() = callbackField

    override fun onDestroy(func: () -> Unit) {
        func()
        callbackField = null
    }
}