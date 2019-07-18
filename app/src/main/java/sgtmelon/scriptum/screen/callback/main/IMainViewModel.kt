package sgtmelon.scriptum.screen.callback.main

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.receiver.MainReceiver
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.screen.vm.main.MainViewModel

/**
 * Интерфейс для общения [MainActivity] с [MainViewModel]
 *
 * @author SerjantArbuz
 */
interface IMainViewModel : MainReceiver.Callback {

    fun onSetupData(bundle: Bundle?)

    fun onSaveData(bundle: Bundle)

    fun onSelectItem(@IdRes itemId: Int): Boolean

}