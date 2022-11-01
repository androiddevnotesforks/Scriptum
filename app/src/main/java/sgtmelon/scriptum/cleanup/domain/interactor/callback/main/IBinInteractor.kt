package sgtmelon.scriptum.cleanup.domain.interactor.callback.main

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModel

/**
 * Interface for communication [BinViewModel] with [BinInteractor].
 */
interface IBinInteractor : IParentInteractor {

    suspend fun getCount(): Int
}