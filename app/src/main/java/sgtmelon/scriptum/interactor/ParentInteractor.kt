package sgtmelon.scriptum.interactor

import android.content.Context
import androidx.annotation.CallSuper
import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Parent class for interactor's
 */
abstract class ParentInteractor(context: Context) {

    protected val iPreferenceRepo: IPreferenceRepo = PreferenceRepo(context)

    /**
     * Same func like in [IParentInteractor] use for clear callback when cause [onDestroy]
     */
    @CallSuper open fun onDestroy(func: () -> Unit = {}) {}

}