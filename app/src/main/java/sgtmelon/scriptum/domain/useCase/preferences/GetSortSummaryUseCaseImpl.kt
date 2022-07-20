package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter

class GetSortSummaryUseCaseImpl(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo,
    private val sortConverter: SortConverter
) : GetSummaryUseCase {

    override fun invoke(): String = summaryProvider.getSort(preferencesRepo.sort)

    override fun invoke(value: Int): String {
        val sort = sortConverter.toEnum(value)
        if (sort != null) {
            preferencesRepo.sort = sort
        }

        return invoke()
    }
}