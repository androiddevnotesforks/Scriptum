package sgtmelon.scriptum.domain.useCase.preferences.summary

import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.provider.SummaryProviderImpl

class GetSortSummaryUseCaseImpl(
    private val summaryProvider: SummaryProviderImpl,
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