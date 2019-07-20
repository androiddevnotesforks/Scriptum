package sgtmelon.scriptum.screen.view.callback

import androidx.annotation.StyleRes
import sgtmelon.scriptum.screen.view.AppActivity
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Интерфейс для обзения [AppViewModel] с [AppActivity]
 *
 * @author SerjantArbuz
 */
interface IAppActivity {

    fun setTheme(@StyleRes resId: Int)

}