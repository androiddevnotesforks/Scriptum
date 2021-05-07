package sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop

import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.ServiceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IServiceViewModel

/**
 * Interface for communication [IServiceViewModel] with [ServiceFragment].
 */
interface IServiceFragment {

    fun setup()


    fun updateRefreshEnabled(isEnabled: Boolean)

    fun resetRefreshSummary()

    fun updateRunEnabled(isEnabled: Boolean)

    fun updateKillEnabled(isEnabled: Boolean)

    fun startPingSummary()

    fun stopPingSummary()

    fun sendPingBroadcast()

    fun sendKillBroadcast()

    fun runService()

}