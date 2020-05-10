package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.DevelopInteractor

/**
 * Interface for [DevelopInteractor].
 */
interface IDevelopInteractor {

    suspend fun getNoteTablePrint(): String

    suspend fun getRollTablePrint(): String

    suspend fun getRankTablePrint(): String

    suspend fun getPreferencePrint(): String

}