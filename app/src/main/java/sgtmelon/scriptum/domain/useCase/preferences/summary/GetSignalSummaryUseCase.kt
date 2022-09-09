package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class GetSignalSummaryUseCase(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo
) {

    operator fun invoke(): String = summaryDataSource.getSignal(preferencesRepo.signalTypeCheck)

    operator fun invoke(valueArray: BooleanArray): String {
        preferencesRepo.signalTypeCheck = valueArray
        return invoke()
    }
}