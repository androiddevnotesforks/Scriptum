package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.provider.SummaryProvider

class GetSignalSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo
) : GetSignalSummaryUseCase {

    override fun invoke(): String = summaryProvider.getSignal(preferencesRepo.signalTypeCheck)

    override fun invoke(valueArray: BooleanArray): String {
        preferencesRepo.signalTypeCheck = valueArray
        return invoke()
    }
}