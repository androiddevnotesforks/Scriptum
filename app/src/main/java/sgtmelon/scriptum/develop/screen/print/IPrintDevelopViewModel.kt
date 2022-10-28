package sgtmelon.scriptum.develop.screen.print

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel

/**
 * Interface for communication [IPrintDevelopActivity] with [PrintDevelopViewModel].
 */
interface IPrintDevelopViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()
}