package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.utils.runBack
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IServiceDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IServiceDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IServiceDevelopFragment].
 */
class ServiceDevelopViewModel(
    callback: IServiceDevelopFragment
) : ParentViewModel<IServiceDevelopFragment>(callback),
    IServiceDevelopViewModel {

    private var pingJob: Job? = null

    override fun onSetup(bundle: Bundle?) {
        callback?.setup()
        onClickRefresh()
    }

    override fun onClickRefresh() {
        pingJob = viewModelScope.launch { startPing() }
    }

    @RunPrivate suspend fun startPing() {
        callback?.updateRefreshEnabled(isEnabled = false)
        callback?.updateRunEnabled(isEnabled = false)
        callback?.updateKillEnabled(isEnabled = false)

        callback?.startPingSummary()

        runBack {
            delay(PING_START_DELAY)
            repeat(PING_REPEAT) {
                callback?.sendPingBroadcast()
                delay(PING_TIMEOUT)
            }
        }

        callback?.stopPingSummary()
        callback?.resetRefreshSummary()

        callback?.updateRefreshEnabled(isEnabled = true)
        callback?.updateRunEnabled(isEnabled = true)
        callback?.updateKillEnabled(isEnabled = false)
    }

    override fun onReceiveEternalServicePong() {
        viewModelScope.launch {
            pingJob?.cancelAndJoin()
            pingJob = null
        }

        callback?.stopPingSummary()
        callback?.resetRefreshSummary()

        callback?.updateRefreshEnabled(isEnabled = true)
        callback?.updateRunEnabled(isEnabled = false)
        callback?.updateKillEnabled(isEnabled = true)
    }

    override fun onClickRun() {
        callback?.runService()
        onClickRefresh()
    }

    override fun onClickKill() {
        callback?.sendKillBroadcast()
        onClickRefresh()
    }

    companion object {
        const val PING_START_DELAY = 3000L
        const val PING_REPEAT = 5
        const val PING_TIMEOUT = 1000L
    }
}