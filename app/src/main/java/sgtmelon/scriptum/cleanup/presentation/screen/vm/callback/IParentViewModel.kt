package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel

/**
 * Parent interface for communicate with children of [ParentViewModel].
 */
interface IParentViewModel {

    @Deprecated("Add converter for bundle data and pass inside viewModels only needed data")
    fun onSetup(bundle: Bundle? = null)

    @Deprecated("Make destroy inside ui")
    fun onDestroy(func: () -> Unit = {})

}