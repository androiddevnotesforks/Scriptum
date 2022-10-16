package sgtmelon.scriptum.infrastructure.develop.screen.develop

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor

class DevelopViewModelImpl(
    private val interactor: DevelopInteractor
) : ViewModel(),
    DevelopViewModel {

    override val randomNoteId: Flow<Long>
        get() = flow { emit(interactor.getRandomNoteId()) }.flowOnBack()

    override fun resetPreferences() = interactor.resetPreferences()

}