package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IServiceDevelopViewModel
import sgtmelon.scriptum.infrastructure.develop.screen.print.ServicePingState

class ServiceDevelopViewModel : ViewModel(),
    IServiceDevelopViewModel {

    private var pingJob: Job? = null

    override val pingState: MutableLiveData<ServicePingState> by lazy {
        MutableLiveData<ServicePingState>().also { startPing() }
    }

    override fun startPing() {
        pingJob = viewModelScope.launch {
            pingState.postValue(ServicePingState.PREPARE)

            runBack {
                delay(PING_START_DELAY)
                repeat(PING_REPEAT) {
                    pingState.postValue(ServicePingState.PING)
                    delay(PING_TIMEOUT)
                }
            }

            pingState.postValue(ServicePingState.NO_RESPONSE)
        }
    }

    override fun cancelPing() {
        viewModelScope.launch {
            pingJob?.cancelAndJoin()
            pingJob = null
        }
    }

    companion object {
        const val PING_START_DELAY = 3000L
        const val PING_REPEAT = 5
        const val PING_TIMEOUT = 1000L
    }
}