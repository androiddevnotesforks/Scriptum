package sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop

import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.ServiceDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IServiceDevelopViewModel

/**
 * Interface for communication [IServiceDevelopViewModel] with [ServiceDevelopFragment].
 */
interface IServiceDevelopFragment {

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