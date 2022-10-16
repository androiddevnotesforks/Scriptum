package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop

import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop.DevelopViewModelImpl

/**
 * Interface for communication [IDevelopFragment] with [DevelopViewModelImpl].
 */
interface DevelopViewModel {

    fun onClickAlarm()

    fun onClickReset()
}