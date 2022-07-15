package sgtmelon.scriptum.cleanup.domain.interactor.impl

import androidx.annotation.CallSuper
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor

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