package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop

import androidx.annotation.StringRes
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.DevelopViewModel

/**
 * Interface for communication [DevelopViewModel] with [DevelopFragment].
 */
interface IDevelopFragment {

    fun showToast(@StringRes stringId: Int)

    fun openAlarmScreen(noteId: Long)

}