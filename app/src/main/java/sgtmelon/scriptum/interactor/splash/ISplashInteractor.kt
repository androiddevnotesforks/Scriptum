package sgtmelon.scriptum.interactor.splash

import sgtmelon.scriptum.control.alarm.callback.AlarmCallback

/**
 * Interface for communicate with [SplashInteractor]
 */
interface ISplashInteractor {

    val firstStart: Boolean

    suspend fun clearPastAlarm(callback: AlarmCallback.Cancel?)

}