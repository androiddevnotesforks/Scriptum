package sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop

import sgtmelon.scriptum.presentation.receiver.screen.DevelopScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IServiceDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop.ServiceDevelopViewModel

/**
 * Interface for communication [IServiceDevelopFragment] with [ServiceDevelopViewModel].
 */
interface IServiceDevelopViewModel : IParentViewModel,
    DevelopScreenReceiver.Callback {

    fun onClickRefresh()

    fun onClickRun()

    fun onClickKill()

}