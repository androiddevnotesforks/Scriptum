package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.domain.model.annotation.test.RunProtected
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel

/**
 * Parent ViewModel.
 *
 * [C] is the interface for communicate with UI. Same like in [IParentViewModel].
 */
abstract class ParentViewModel<C>(application: Application) :
    AndroidViewModel(application),
    IParentViewModel {

    protected val context: Context = application.applicationContext

    @RunProtected
    var callback: C? = null
        private set

    /**
     * Call this func from when create viewModel.
     */
    @CallSuper fun setCallback(callback: C?) {
        this.callback = callback
    }

    override fun onDestroy(func: () -> Unit) {
        func()
        callback = null
    }

}