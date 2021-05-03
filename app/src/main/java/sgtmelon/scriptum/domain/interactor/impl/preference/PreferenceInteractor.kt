package sgtmelon.scriptum.domain.interactor.impl.preference

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPreferenceViewModel

/**
 * Interactor for [IPreferenceViewModel].
 */
class PreferenceInteractor(
    private val summaryProvider: SummaryProvider,
    private val preferenceRepo: IPreferenceRepo
) : IPreferenceInteractor {

    @Theme override val theme: Int get() = preferenceRepo.theme

    override fun getThemeSummary(): String? = summaryProvider.theme.getOrNull(theme)

    override fun updateTheme(@Theme value: Int): String? {
        preferenceRepo.theme = value
        return getThemeSummary()
    }


    override var isDeveloper: Boolean
        get() = preferenceRepo.isDeveloper
        set(value) {
            preferenceRepo.isDeveloper = value
        }
}