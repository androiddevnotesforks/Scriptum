package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.PreferenceViewModel

/**
 * Interface for communication [IPreferenceFragment] with [PreferenceViewModel].
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onClickTheme()

    fun onResultTheme(@Theme value: Int)

    fun onUnlockDeveloper()

}