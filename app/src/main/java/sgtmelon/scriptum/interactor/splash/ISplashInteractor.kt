package sgtmelon.scriptum.interactor.splash

import sgtmelon.scriptum.control.alarm.callback.AlarmCallback

/**
 * Interface for communication with [SplashInteractor]
 *
 * @author SerjantArbuz
 */
interface ISplashInteractor {

    val firstStart: Boolean

    suspend fun clearPastAlarm(callback: AlarmCallback.Cancel?)

}