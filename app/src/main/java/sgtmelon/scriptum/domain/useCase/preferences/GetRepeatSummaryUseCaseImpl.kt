package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.provider.SummaryProvider

class GetRepeatSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo,
    private val converter: RepeatConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getRepeat(preferencesRepo.repeat)

    override fun invoke(value: Int): String {
        val repeat = converter.toEnum(value)
        if (repeat != null) {
            preferencesRepo.repeat = repeat
        }

        return invoke()
    }
}