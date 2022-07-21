package sgtmelon.scriptum.domain.useCase.preferences

import androidx.annotation.IntRange
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class GetVolumeSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getVolume(preferencesRepo.volume)

    override fun invoke(@IntRange(from = 10, to = 100) value: Int): String {
        preferencesRepo.volume = value
        return invoke()
    }
}