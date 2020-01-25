package sgtmelon.scriptum.interactor

import androidx.annotation.CallSuper
import sgtmelon.scriptum.interactor.callback.IParentInteractor

/**
 * Parent class for interactor's.
 */
abstract class ParentInteractor {

    /**
     * Same func like in [IParentInteractor], use for clear callback when cause onDestroy.
     */
    @CallSuper open fun onDestroy(func: () -> Unit = {}) {}

}