package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Interactor for [INotePreferenceViewModel].
 */
class NotePreferenceInteractor(
    private val summaryProvider: SummaryProvider,
    private val preferences: Preferences
) : INotePreferenceInteractor {

    @Color override val defaultColor: Int get() = preferences.defaultColor

    override fun getDefaultColorSummary(): String? = summaryProvider.color.getOrNull(defaultColor)

    override fun updateDefaultColor(@Color value: Int): String? {
        preferences.defaultColor = value
        return getDefaultColorSummary()
    }


    @SavePeriod override val savePeriod: Int get() = preferences.savePeriod

    override fun getSavePeriodSummary(): String? = summaryProvider.savePeriod.getOrNull(savePeriod)

    override fun updateSavePeriod(@SavePeriod value: Int): String? {
        preferences.savePeriod = value
        return getSavePeriodSummary()
    }

}