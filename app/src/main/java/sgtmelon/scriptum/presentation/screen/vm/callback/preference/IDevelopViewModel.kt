package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.DevelopViewModel

/**
 * Interface for communication [IDevelopFragment] with [DevelopViewModel]
 */
interface IDevelopViewModel : IParentViewModel {

    fun onClickPrint(type: PrintType)

    fun onClickAlarm()

    fun onClickReset()
}