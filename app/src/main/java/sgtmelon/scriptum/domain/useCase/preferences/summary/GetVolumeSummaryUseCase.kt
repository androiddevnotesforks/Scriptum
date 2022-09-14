package sgtmelon.scriptum.domain.useCase.preferences.summary

import androidx.annotation.IntRange
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class GetVolumeSummaryUseCase(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo
) : GetSummaryUseCase {

    override operator fun invoke(): String {
        return summaryDataSource.getVolume(preferencesRepo.volumePercent)
    }

    override operator fun invoke(@IntRange(from = 10, to = 100) value: Int): String {
        preferencesRepo.volumePercent = value
        return invoke()
    }
}