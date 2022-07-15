package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop

import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop.DevelopViewModel

/**
 * Interface for communication [IDevelopFragment] with [DevelopViewModel].
 */
interface IDevelopViewModel : IParentViewModel {

    fun onClickPrint(type: PrintType)

    fun onClickAlarm()

    fun onClickReset()
}