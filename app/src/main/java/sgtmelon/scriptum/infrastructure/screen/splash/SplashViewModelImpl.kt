package sgtmelon.scriptum.infrastructure.screen.splash

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

// TODO rename and leave only preferencesRepo
class SplashViewModelImpl(
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    val isFirstStart get() = preferencesRepo.isFirstStart

}