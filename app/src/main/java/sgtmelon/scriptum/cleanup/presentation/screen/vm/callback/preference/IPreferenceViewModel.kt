package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.key.Theme

interface IPreferenceViewModel {

    val isDeveloper: LiveData<Boolean>

    val theme: Theme

    val themeSummary: LiveData<String>

    fun updateTheme(value: Int)

    fun unlockDeveloper()

}