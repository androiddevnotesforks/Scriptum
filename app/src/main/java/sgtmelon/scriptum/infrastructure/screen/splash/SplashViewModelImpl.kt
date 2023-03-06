package sgtmelon.scriptum.infrastructure.screen.splash

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

// TODO tests
class SplashViewModelImpl(private val preferencesRepo: PreferencesRepo) : ViewModel(),
    SplashViewModel {

    override val defaultColor: Color get() = preferencesRepo.defaultColor

}