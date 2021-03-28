package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import android.os.Bundle
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPrintActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.PrintViewModel

/**
 * Interface for communication [IPrintActivity] with [PrintViewModel].
 */
interface IPrintViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()
}