package sgtmelon.scriptum.presentation.screen.ui.callback

import androidx.annotation.StyleRes
import sgtmelon.scriptum.presentation.screen.ui.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.AppViewModel

/**
 * Interface for communication [AppViewModel] with [AppActivity]
 */
interface IAppActivity {

    fun setTheme(@StyleRes resId: Int)

}