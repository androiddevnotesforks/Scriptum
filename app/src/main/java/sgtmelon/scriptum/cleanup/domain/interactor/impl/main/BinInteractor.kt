package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.infrastructure.screen.main.bin.IBinViewModel

/**
 * Interactor for [IBinViewModel].
 */
class BinInteractor(private val noteRepo: NoteRepo) : ParentInteractor(),
    IBinInteractor {

    override suspend fun getCount(): Int = noteRepo.getCount(isBin = true)
}