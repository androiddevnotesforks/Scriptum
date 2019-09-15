package sgtmelon.scriptum.interactor.splash

import sgtmelon.scriptum.control.alarm.callback.IAlarmBridge

/**
 * Interface for communicate with [SplashInteractor]
 */
interface ISplashInteractor {

    val firstStart: Boolean

    suspend fun clearPastAlarm(callback: IAlarmBridge.Cancel?)

}