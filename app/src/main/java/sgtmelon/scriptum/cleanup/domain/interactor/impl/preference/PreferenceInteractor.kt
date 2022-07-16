package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel

/**
 * Interactor for [IPreferenceViewModel].
 */
class PreferenceInteractor(
    private val summaryProvider: SummaryProvider,
    private val preferences: Preferences
) : IPreferenceInteractor {

    @Theme override val theme: Int get() = preferences.theme

    override fun getThemeSummary(): String? = summaryProvider.theme.getOrNull(theme)

    override fun updateTheme(@Theme value: Int): String? {
        preferences.theme = value
        return getThemeSummary()
    }


    override var isDeveloper: Boolean
        get() = preferences.isDeveloper
        set(value) {
            preferences.isDeveloper = value
        }
}