package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class GetSignalSummaryUseCaseImpl(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo
) : GetSignalSummaryUseCase {

    override fun invoke(): String = summaryDataSource.getSignal(preferencesRepo.signalTypeCheck)

    override fun invoke(valueArray: BooleanArray): String {
        preferencesRepo.signalTypeCheck = valueArray
        return invoke()
    }
}