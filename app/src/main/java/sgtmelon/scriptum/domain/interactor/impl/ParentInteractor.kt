package sgtmelon.scriptum.domain.interactor.impl

import androidx.annotation.CallSuper
import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor

/**
 * Parent class for interactor's.
 */
abstract class ParentInteractor {

    /**
     * Same func like in [IParentInteractor], use for clear callback when cause onDestroy.
     */
    @CallSuper open fun onDestroy(func: () -> Unit = {}) {
        func()
    }

}