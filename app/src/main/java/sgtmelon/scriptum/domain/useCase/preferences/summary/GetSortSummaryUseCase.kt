package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter

class GetSortSummaryUseCase(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val converter: SortConverter
) : GetSummaryUseCase {

    override operator fun invoke(): String = summaryDataSource.getSort(preferencesRepo.sort)

    override operator fun invoke(value: Int): String {
        val sort = converter.toEnum(value)
        if (sort != null) {
            preferencesRepo.sort = sort
        }

        return invoke()
    }
}