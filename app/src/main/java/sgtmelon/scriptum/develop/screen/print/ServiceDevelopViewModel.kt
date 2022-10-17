package sgtmelon.scriptum.develop.screen.print

import androidx.lifecycle.LiveData

interface ServiceDevelopViewModel {

    val pingState: LiveData<ServicePingState>

    fun startPing()

    fun cancelPing()

}