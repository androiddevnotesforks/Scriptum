package sgtmelon.scriptum.presentation.service.presenter

import androidx.annotation.CallSuper
import sgtmelon.scriptum.domain.model.annotation.test.RunProtected

/**
 * Basic class for all presenters with callback.
 */
abstract class ParentPresenter<C> : IParentPresenter {

    @RunProtected
    var callback: C? = null
        private set

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