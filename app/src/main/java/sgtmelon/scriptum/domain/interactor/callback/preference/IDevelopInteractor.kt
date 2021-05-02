package sgtmelon.scriptum.domain.interactor.callback.preference

import sgtmelon.scriptum.domain.interactor.impl.preference.DevelopInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel

/**
 * Interface for communication [IDevelopViewModel] with [DevelopInteractor].
 */
interface IDevelopInteractor {

    // TODO

    suspend fun getRandomNoteId(): Long

    fun resetPreferences()

}