package sgtmelon.scriptum.develop.infrastructure.screen.service

import androidx.lifecycle.LiveData

interface ServiceDevelopViewModel {

    val pingState: LiveData<ServicePingState>

    fun startPing()

    fun interruptPing()

}