package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter

class GetDefaultColorSummaryUseCase(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val converter: ColorConverter
) : GetSummaryUseCase {

    override operator fun invoke(): String {
        return summaryDataSource.getColor(preferencesRepo.defaultColor)
    }

    override operator fun invoke(value: Int): String {
        val color = converter.toEnum(value)
        if (color != null) {
            preferencesRepo.defaultColor = color
        }

        return invoke()
    }
}