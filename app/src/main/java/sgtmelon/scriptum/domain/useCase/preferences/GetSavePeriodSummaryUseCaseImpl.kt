package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter

class GetSavePeriodSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo,
    private val converter: SavePeriodConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getSavePeriod(preferencesRepo.savePeriod)

    override fun invoke(value: Int): String {
        val savePeriod = converter.toEnum(value)
        if (savePeriod != null) {
            preferencesRepo.savePeriod = savePeriod
        }

        return invoke()
    }
}