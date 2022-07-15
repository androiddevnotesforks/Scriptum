package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop

import sgtmelon.scriptum.cleanup.presentation.receiver.screen.DevelopScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IServiceDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop.ServiceDevelopViewModel

/**
 * Interface for communication [IServiceDevelopFragment] with [ServiceDevelopViewModel].
 */
interface IServiceDevelopViewModel : IParentViewModel,
    DevelopScreenReceiver.Callback {

    fun onClickRefresh()

    fun onClickRun()

    fun onClickKill()

}