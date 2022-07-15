package sgtmelon.scriptum.cleanup.presentation.screen.presenter

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import sgtmelon.common.test.annotation.RunProtected

/**
 * Basic class for all presenters with callback.
 */
abstract class ParentPresenter<C> : IParentPresenter {

    @RunProtected
    var callback: C? = null
        private set

    // TODO change dispatcher
    @RunProtected val mainScope by lazy { CoroutineScope(Dispatchers.Main) }

    /**
     * Call this func when create presenter.
     */
    @CallSuper fun setCallback(callback: C?) {
        this.callback = callback
    }

    override fun onDestroy(func: () -> Unit) {
        func()
        callback = null
    }
}