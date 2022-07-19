package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.ThemeConverter

class GetThemeSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo,
    private val themeConverter: ThemeConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getTheme(preferencesRepo.theme)

    override fun invoke(value: Int): String {
        val theme = themeConverter.toEnum(value)
        if (theme != null) {
            preferencesRepo.theme = theme
        }

        return invoke()
    }
}