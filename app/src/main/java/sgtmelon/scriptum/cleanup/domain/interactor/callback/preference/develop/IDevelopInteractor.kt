package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop

import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop.DevelopInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IDevelopViewModel

/**
 * Interface for communication [IDevelopViewModel] with [DevelopInteractor].
 */
interface IDevelopInteractor {

    // TODO

    suspend fun getRandomNoteId(): Long

    fun resetPreferences()

}