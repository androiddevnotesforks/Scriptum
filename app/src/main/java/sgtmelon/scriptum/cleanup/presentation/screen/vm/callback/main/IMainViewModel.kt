package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.MainViewModel

/**
 * Interface for communication [IMainActivity] with [MainViewModel].
 */
interface IMainViewModel : IParentViewModel {

    val isStartPage: Boolean

    fun onSaveData(bundle: Bundle)

    fun onSelectItem(@IdRes itemId: Int)

}