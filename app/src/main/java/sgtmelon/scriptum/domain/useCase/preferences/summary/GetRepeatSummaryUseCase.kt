package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter

class GetRepeatSummaryUseCase(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val converter: RepeatConverter
) : GetSummaryUseCase {

    override operator fun invoke(): String = summaryDataSource.getRepeat(preferencesRepo.repeat)

    override operator fun invoke(value: Int): String {
        val repeat = converter.toEnum(value)
        if (repeat != null) {
            preferencesRepo.repeat = repeat
        }

        return invoke()
    }
}