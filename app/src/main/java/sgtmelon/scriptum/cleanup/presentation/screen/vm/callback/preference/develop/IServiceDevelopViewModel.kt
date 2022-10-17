package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.develop.screen.print.ServicePingState

interface IServiceDevelopViewModel {

    val pingState: LiveData<ServicePingState>

    fun startPing()

    fun cancelPing()

}