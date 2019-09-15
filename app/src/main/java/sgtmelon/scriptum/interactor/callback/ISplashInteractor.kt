package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.SplashInteractor

/**
 * Interface for communicate with [SplashInteractor]
 */
interface ISplashInteractor : IParentInteractor {

    val firstStart: Boolean

    suspend fun clearPastAlarm()

}