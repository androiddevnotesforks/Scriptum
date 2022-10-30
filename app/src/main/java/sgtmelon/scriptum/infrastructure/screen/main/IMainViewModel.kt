package sgtmelon.scriptum.infrastructure.screen.main

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel

/**
 * Interface for communication [IMainActivity] with [MainViewModelImpl].
 */
interface IMainViewModel : IParentViewModel {

    val isStartPage: Boolean

    fun onSaveData(bundle: Bundle)

    fun onSelectItem(@IdRes itemId: Int)

}