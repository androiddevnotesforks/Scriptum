package sgtmelon.scriptum.domain.interactor.impl.preference

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.INotePreferenceViewModel

/**
 * Interactor for [INotePreferenceViewModel].
 */
class NotePreferenceInteractor(
    private val summaryProvider: SummaryProvider,
    private val preferenceRepo: IPreferenceRepo
) : INotePreferenceInteractor {

    @Sort override val sort: Int get() = preferenceRepo.sort

    override fun getSortSummary(): String? = summaryProvider.sort.getOrNull(sort)

    override fun updateSort(@Sort value: Int): String? {
        preferenceRepo.sort = value
        return getSortSummary()
    }


    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor

    override fun getDefaultColorSummary(): String? = summaryProvider.color.getOrNull(defaultColor)

    override fun updateDefaultColor(@Color value: Int): String? {
        preferenceRepo.defaultColor = value
        return getDefaultColorSummary()
    }


    override val savePeriod: Int get() = preferenceRepo.savePeriod

    override fun getSavePeriodSummary(): String? = summaryProvider.savePeriod.getOrNull(savePeriod)

    override fun updateSavePeriod(value: Int): String? {
        preferenceRepo.savePeriod = value
        return getSavePeriodSummary()
    }

}