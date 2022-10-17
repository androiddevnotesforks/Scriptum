package sgtmelon.scriptum.infrastructure.develop.screen.print

import androidx.lifecycle.LiveData

interface IServiceDevelopViewModel {

    val pingState: LiveData<ServicePingState>

    fun startPing()

    fun cancelPing()

}