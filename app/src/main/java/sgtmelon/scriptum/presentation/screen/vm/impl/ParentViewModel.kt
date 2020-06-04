package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel

/**
 * Parent ViewModel.
 *
 * [T] is the interface for communicate with UI. Same like in [IParentViewModel].
 */
abstract class ParentViewModel<T>(application: Application) : AndroidViewModel(application),
        IParentViewModel {

    protected val context: Context = application.applicationContext

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var callback: T? = null
        private set

    /**
     * Call this func from when create viewModel.
     */
    @CallSuper fun setCallback(callback: T?) {
        this.callback = callback
    }

    override fun onDestroy(func: () -> Unit) {
        func()
        callback = null
    }

}