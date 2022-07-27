package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

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