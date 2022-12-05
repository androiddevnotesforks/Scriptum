package sgtmelon.scriptum.infrastructure.screen.theme

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme

class ThemeViewModelImpl(private val preferencesRepo: PreferencesRepo) : ViewModel(),
    ThemeViewModel {

    override val theme: Theme get() = preferencesRepo.theme

}