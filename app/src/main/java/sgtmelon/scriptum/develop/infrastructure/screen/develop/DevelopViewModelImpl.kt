package sgtmelon.scriptum.develop.infrastructure.screen.develop

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.scriptum.develop.domain.DevelopInteractor

class DevelopViewModelImpl(
    private val interactor: DevelopInteractor
) : ViewModel(),
    DevelopViewModel {

    override val randomNoteId: Flow<Long>
        get() = flowOnBack { emit(interactor.getRandomNoteId()) }

    override fun resetPreferences() = interactor.resetPreferences()

}