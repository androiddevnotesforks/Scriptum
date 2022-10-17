package sgtmelon.scriptum.develop.screen.service

import androidx.lifecycle.LiveData

interface ServiceDevelopViewModel {

    val pingState: LiveData<ServicePingState>

    fun startPing()

    fun cancelPing()

}