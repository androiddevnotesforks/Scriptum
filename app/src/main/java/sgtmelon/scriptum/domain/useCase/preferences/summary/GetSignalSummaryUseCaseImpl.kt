package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.provider.SummaryProviderImpl

class GetSignalSummaryUseCaseImpl(
    private val summaryProvider: SummaryProviderImpl,
    private val preferencesRepo: PreferencesRepo
) : GetSignalSummaryUseCase {

    override fun invoke(): String = summaryProvider.getSignal(preferencesRepo.signalTypeCheck)

    override fun invoke(valueArray: BooleanArray): String {
        preferencesRepo.signalTypeCheck = valueArray
        return invoke()
    }
}