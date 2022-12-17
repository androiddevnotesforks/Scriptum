package sgtmelon.scriptum.cleanup.presentation.screen

import android.os.Bundle

/**
 * Parent interface for communicate with children of [ParentViewModel].
 */
@Deprecated("Remove this")
interface IParentViewModel {

    @Deprecated("Add converter for bundle data and pass inside viewModels only needed data")
    fun onSetup(bundle: Bundle? = null)

    @Deprecated("Make destroy inside ui")
    fun onDestroy(func: () -> Unit = {})

}