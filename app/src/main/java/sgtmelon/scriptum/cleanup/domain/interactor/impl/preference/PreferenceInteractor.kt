package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import com.google.firebase.crashlytics.FirebaseCrashlytics
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Interactor for [IPreferenceViewModel].
 */
class PreferenceInteractor(
    private val summaryProvider: SummaryProvider,
    private val preferencesRepo: PreferencesRepo,
    private val themeConverter: ThemeConverter
) : IPreferenceInteractor {

    override fun getThemeSummary(): String = summaryProvider.getTheme(preferencesRepo.theme)

    // TODO test crashlytics send not fatal error
    override fun updateTheme(value: Int): String {
        val theme = themeConverter.toEnum(value)
        if (theme != null) {
            preferencesRepo.theme = theme
        } else {
            val crashlytics = FirebaseCrashlytics.getInstance()
            crashlytics.setCustomKey("${Theme::class.simpleName}", value)
            crashlytics.setCustomKey("inPreferences", preferencesRepo.theme.name)
            crashlytics.recordException(Throwable())
        }

        return getThemeSummary()
    }
}