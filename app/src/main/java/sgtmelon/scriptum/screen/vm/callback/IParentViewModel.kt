package sgtmelon.scriptum.screen.vm.callback

import android.os.Bundle
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * Parent interface for communication with children of [ParentViewModel]
 */
interface IParentViewModel {

    fun onSetup(bundle: Bundle? = null)

    fun onDestroy(func: () -> Unit = {})

}