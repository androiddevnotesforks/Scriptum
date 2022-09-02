package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter

class GetSavePeriodSummaryUseCaseImpl(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val converter: SavePeriodConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryDataSource.getSavePeriod(preferencesRepo.savePeriod)

    override fun invoke(value: Int): String {
        val savePeriod = converter.toEnum(value)
        if (savePeriod != null) {
            preferencesRepo.savePeriod = savePeriod
        }

        return invoke()
    }
}