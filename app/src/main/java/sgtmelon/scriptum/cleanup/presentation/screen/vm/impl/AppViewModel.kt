package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.Theme

class AppViewModel(private val preferencesRepo: PreferencesRepo) : ViewModel(),
    IAppViewModel {

    override var theme: Theme = preferencesRepo.theme

    override fun isThemeChanged(): Boolean {
        val newTheme = preferencesRepo.theme
        val isChanged = theme != newTheme
        theme = newTheme

        return isChanged
    }
}