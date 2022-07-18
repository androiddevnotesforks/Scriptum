package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.PreferenceViewModel

/**
 * Interface for communication [IPreferenceFragment] with [PreferenceViewModel].
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onClickTheme()

    fun onResultTheme(value: Int)

    fun onUnlockDeveloper()

}