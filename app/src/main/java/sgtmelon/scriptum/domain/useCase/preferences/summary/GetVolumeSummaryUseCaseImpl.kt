package sgtmelon.scriptum.domain.useCase.preferences.summary

import androidx.annotation.IntRange
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class GetVolumeSummaryUseCaseImpl(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo
) : GetSummaryUseCase {

    override fun invoke(): String = summaryDataSource.getVolume(preferencesRepo.volume)

    override fun invoke(@IntRange(from = 10, to = 100) value: Int): String {
        preferencesRepo.volume = value
        return invoke()
    }
}