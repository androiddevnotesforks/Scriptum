package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.DevelopInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.IDevelopViewModel

/**
 * Interface for communication [IDevelopViewModel] with [DevelopInteractor].
 */
interface IDevelopInteractor {

    suspend fun getNoteTablePrint(): String

    suspend fun getRollTablePrint(): String

    suspend fun getRankTablePrint(): String

    suspend fun getPreferencePrint(): String

}