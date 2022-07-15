package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.PreferenceViewModel

/**
 * Interface for communication [IPreferenceFragment] with [PreferenceViewModel].
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onClickTheme()

    fun onResultTheme(@Theme value: Int)

    fun onUnlockDeveloper()

}