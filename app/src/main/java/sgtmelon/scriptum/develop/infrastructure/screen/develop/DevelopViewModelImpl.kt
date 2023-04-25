package sgtmelon.scriptum.develop.infrastructure.screen.develop

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowIO
import sgtmelon.scriptum.develop.domain.GetRandomNoteIdUseCase
import sgtmelon.scriptum.develop.domain.ResetPreferencesUseCase

class DevelopViewModelImpl(
    private val getRandomNoteId: GetRandomNoteIdUseCase,
    private val resetPreferences: ResetPreferencesUseCase
) : ViewModel(),
    DevelopViewModel {

    override val randomNoteId: Flow<Long>
        get() = flowIO { emit(getRandomNoteId()) }

    override fun resetPreferences() = resetPreferences.invoke()

}