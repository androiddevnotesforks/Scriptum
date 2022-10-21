package sgtmelon.scriptum.infrastructure.screen.preference.menu

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme

interface MenuPreferenceViewModel {

    val theme: Theme

    val themeSummary: LiveData<String>

    fun updateTheme(value: Int)

    val isDeveloper: LiveData<Boolean>

    fun unlockDeveloper()

}