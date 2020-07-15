package sgtmelon.scriptum.presentation.screen.ui.callback

import androidx.annotation.StyleRes
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel

/**
 * Interface for communication [IAppViewModel] with [AppActivity].
 */
interface IAppActivity {
    fun setTheme(@StyleRes resId: Int)
}