package sgtmelon.scriptum.interactor.splash

import sgtmelon.scriptum.interactor.IParentInteractor

/**
 * Interface for communicate with [SplashInteractor]
 */
interface ISplashInteractor : IParentInteractor {

    val firstStart: Boolean

    suspend fun clearPastAlarm()

}