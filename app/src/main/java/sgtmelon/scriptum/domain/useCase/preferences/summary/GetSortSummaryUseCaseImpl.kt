package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter

class GetSortSummaryUseCaseImpl(
    private val summaryDataSource: SummaryDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val converter: SortConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryDataSource.getSort(preferencesRepo.sort)

    override fun invoke(value: Int): String {
        val sort = converter.toEnum(value)
        if (sort != null) {
            preferencesRepo.sort = sort
        }

        return invoke()
    }
}