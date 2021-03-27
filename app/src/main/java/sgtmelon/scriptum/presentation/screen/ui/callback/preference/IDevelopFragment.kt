package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.DevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IDevelopViewModel

/**
 * Interface for communication [IDevelopViewModel] with [DevelopFragment]
 */
interface IDevelopFragment {

    fun setupPrints()

    fun setupScreens()

    fun setupOther()

    fun showToast(@StringRes stringId: Int)


    fun openAlarmScreen(noteId: Long)
}