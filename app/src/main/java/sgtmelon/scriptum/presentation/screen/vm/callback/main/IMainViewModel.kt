package sgtmelon.scriptum.presentation.screen.vm.callback.main

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.presentation.receiver.MainReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.MainViewModel

/**
 * Interface for communication [MainActivity] with [MainViewModel]
 */
interface IMainViewModel : IParentViewModel, MainReceiver.Callback {

    fun onSaveData(bundle: Bundle)

    fun onSelectItem(@IdRes itemId: Int)

    fun onFabStateChange(state: Boolean)

    fun onResultAddDialog(@IdRes itemId: Int)

}