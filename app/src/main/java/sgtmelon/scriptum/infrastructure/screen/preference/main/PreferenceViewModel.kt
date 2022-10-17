package sgtmelon.scriptum.infrastructure.screen.preference.main

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.key.Theme

interface PreferenceViewModel {

    val isDeveloper: LiveData<Boolean>

    val theme: Theme

    val themeSummary: LiveData<String>

    fun updateTheme(value: Int)

    fun unlockDeveloper()

}