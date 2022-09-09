package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter

class GetSavePeriodSummaryUseCase(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val converter: SavePeriodConverter
) : GetSummaryUseCase {

    override operator fun invoke(): String {
        return summaryDataSource.getSavePeriod(preferencesRepo.savePeriod)
    }

    override operator fun invoke(value: Int): String {
        val savePeriod = converter.toEnum(value)
        if (savePeriod != null) {
            preferencesRepo.savePeriod = savePeriod
        }

        return invoke()
    }
}