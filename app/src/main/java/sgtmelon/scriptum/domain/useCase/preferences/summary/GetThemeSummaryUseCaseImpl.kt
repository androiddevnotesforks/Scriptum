package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter

class GetThemeSummaryUseCaseImpl(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val converter: ThemeConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryDataSource.getTheme(preferencesRepo.theme)

    override fun invoke(value: Int): String {
        val theme = converter.toEnum(value)
        if (theme != null) {
            preferencesRepo.theme = theme
        }

        return invoke()
    }
}