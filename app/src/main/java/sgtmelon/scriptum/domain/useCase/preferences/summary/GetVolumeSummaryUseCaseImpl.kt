package sgtmelon.scriptum.domain.useCase.preferences.summary

import androidx.annotation.IntRange
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.provider.SummaryProviderImpl

class GetVolumeSummaryUseCaseImpl(
    private val summaryProvider: SummaryProviderImpl,
    private val preferencesRepo: PreferencesRepo
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getVolume(preferencesRepo.volume)

    override fun invoke(@IntRange(from = 10, to = 100) value: Int): String {
        preferencesRepo.volume = value
        return invoke()
    }
}