package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference

import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.PreferenceInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel

/**
 * Interface for communication [IPreferenceViewModel] with [PreferenceInteractor].
 */
interface IPreferenceInteractor {

    fun getThemeSummary(): String

    fun updateTheme(value: Int): String
}