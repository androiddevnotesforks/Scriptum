package sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop

import sgtmelon.scriptum.presentation.receiver.screen.DevelopScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IServiceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop.ServiceViewModel

/**
 * Interface for communication [IServiceFragment] with [ServiceViewModel].
 */
interface IServiceViewModel : IParentViewModel,
    DevelopScreenReceiver.Callback {

    fun onClickRefresh()

    fun onClickRun()

    fun onClickKill()

}