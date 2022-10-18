package sgtmelon.scriptum.infrastructure.screen.preference.main

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.key.Theme

interface PreferenceViewModel {

    val theme: Theme

    val themeSummary: LiveData<String>

    fun updateTheme(value: Int)

    val isDeveloper: LiveData<Boolean>

    fun unlockDeveloper()

}