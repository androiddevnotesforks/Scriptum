package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.provider.SummaryProvider

class GetSortSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo,
    private val converter: SortConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getSort(preferencesRepo.sort)

    override fun invoke(value: Int): String {
        val sort = converter.toEnum(value)
        if (sort != null) {
            preferencesRepo.sort = sort
        }

        return invoke()
    }
}