package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter

class GetDefaultColorSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo,
    private val converter: ColorConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getColor(preferencesRepo.defaultColor)

    override fun invoke(value: Int): String {
        val color = converter.toEnum(value)
        if (color != null) {
            preferencesRepo.defaultColor = color
        }

        return invoke()
    }
}