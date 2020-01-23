package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel

/**
 * Parent ViewModel.
 *
 * [T] is the interface for communicate with UI. Same like in [IParentViewModel].
 */
abstract class ParentViewModel<T>(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var callback: T? = null
        private set

    /**
     * Call this func from [ViewModelFactory].
     */
    @CallSuper open fun setCallback(callback: T?) {
        this.callback = callback
    }

    /**
     * Same func like in [IParentViewModel].
     */
    @CallSuper open fun onDestroy(func: () -> Unit = {}) {
        func()
        callback = null
    }

}