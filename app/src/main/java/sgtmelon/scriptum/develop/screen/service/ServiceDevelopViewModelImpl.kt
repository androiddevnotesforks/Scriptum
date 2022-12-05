package sgtmelon.scriptum.develop.screen.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import sgtmelon.extensions.launchBack

class ServiceDevelopViewModelImpl : ViewModel(),
    ServiceDevelopViewModel {

    private var pingJob: Job? = null

    override val pingState by lazy { MutableLiveData<ServicePingState>().also { startPing() } }

    override fun startPing() {
        pingJob = viewModelScope.launchBack {
            pingState.postValue(ServicePingState.PREPARE)

            delay(PING_START_DELAY)
            repeat(PING_REPEAT) {
                pingState.postValue(ServicePingState.PING)
                delay(PING_TIMEOUT)
            }

            pingState.postValue(ServicePingState.NO_RESPONSE)
        }
    }

    override fun interruptPing() {
        pingJob?.cancel()
        pingJob = null

        pingState.value = ServicePingState.SUCCESS
    }

    companion object {
        const val PING_START_DELAY = 3000L
        const val PING_REPEAT = 5
        const val PING_TIMEOUT = 1000L
    }
}