package sgtmelon.scriptum.infrastructure.screen.theme

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme

class ThemeViewModelImpl(private val preferencesRepo: PreferencesRepo) : ViewModel(),
    ThemeViewModel {

    /**
     * Inside [ThemeViewModel] it's val but here it's var. :)
     */
    override var theme: Theme = preferencesRepo.theme

    override fun isThemeChanged(): Boolean {
        val newTheme = preferencesRepo.theme

        val isChanged = theme != newTheme
        if (isChanged) {
            theme = newTheme
        }

        return isChanged
    }
}