package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.callback.IParentInteractor

/**
 * Interface for communicate with [SplashInteractor]
 */
interface ISplashInteractor : IParentInteractor {

    val firstStart: Boolean

    suspend fun clearPastAlarm()

}