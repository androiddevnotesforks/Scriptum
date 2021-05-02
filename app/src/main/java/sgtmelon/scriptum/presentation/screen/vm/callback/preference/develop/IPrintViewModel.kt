package sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop

import android.os.Bundle
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IPrintActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop.PrintViewModel

/**
 * Interface for communication [IPrintActivity] with [PrintViewModel].
 */
interface IPrintViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()
}