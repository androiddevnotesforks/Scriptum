package sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop

import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel

/**
 * Interface for communication [IDevelopViewModel] with [DevelopFragment].
 */
interface IDevelopFragment {

    fun setupPrints()

    fun setupScreens()

    fun setupEternal()

    fun setupOther()

    fun showToast(@StringRes stringId: Int)


    fun openPrintScreen(type: PrintType)

    fun openAlarmScreen(noteId: Long)

    //region Eternal functions

    fun updateEternalRefreshEnabled(isEnabled: Boolean)

    fun resetEternalRefreshSummary()

    fun updateEternalRunEnabled(isEnabled: Boolean)

    fun updateEternalKillEnabled(isEnabled: Boolean)

    fun startEternalPingSummary()

    fun stopEternalPingSummary()

    fun sendEternalPingBroadcast()

    fun sendEternalKillBroadcast()

    fun runEternalService()

    //endregion

}