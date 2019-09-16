package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel

/**
 * Parent ViewModel
 */
abstract class ParentViewModel<T>(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    var callback: T? = null

    /**
     * Same func like in [IParentViewModel] use for clear [callback] when cause [onDestroy]
     */
    @CallSuper open fun onDestroy(func: () -> Unit = {}) {
        func()
        callback = null
    }

}