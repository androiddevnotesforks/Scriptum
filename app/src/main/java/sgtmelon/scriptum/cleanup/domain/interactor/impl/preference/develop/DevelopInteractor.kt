package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop

import sgtmelon.scriptum.cleanup.data.repository.room.callback.DevelopRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Interactor for [IDevelopViewModel].
 */
class DevelopInteractor(
    private val developRepo: DevelopRepo,
    private val preferences: Preferences
) : ParentInteractor(),
    IDevelopInteractor {

    override suspend fun getRandomNoteId(): Long = developRepo.getRandomNoteId()

    override fun resetPreferences() = preferences.clear()
}