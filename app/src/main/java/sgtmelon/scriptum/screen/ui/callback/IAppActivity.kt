package sgtmelon.scriptum.screen.ui.callback

import androidx.annotation.StyleRes
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Interface for communication [AppViewModel] with [AppActivity]
 */
interface IAppActivity {

    fun setTheme(@StyleRes resId: Int)

}