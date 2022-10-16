package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.DevelopViewModel
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor

class DevelopViewModelImpl(
    private val interactor: DevelopInteractor
) : ViewModel(),
    DevelopViewModel {

    override val randomNoteId: Flow<Long>
        get() = flow<Long> { interactor.getRandomNoteId() }.flowOnBack()

    override fun resetPreferences() = interactor.resetPreferences()

}