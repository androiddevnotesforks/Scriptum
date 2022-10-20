package sgtmelon.scriptum.develop.screen.develop

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sgtmelon.extensions.onBack
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor

class DevelopViewModelImpl(
    private val interactor: DevelopInteractor
) : ViewModel(),
    DevelopViewModel {

    override val randomNoteId: Flow<Long>
        get() = flow { emit(interactor.getRandomNoteId()) }.onBack()

    override fun resetPreferences() = interactor.resetPreferences()

}