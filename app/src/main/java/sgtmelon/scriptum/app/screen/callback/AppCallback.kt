package sgtmelon.scriptum.app.screen.callback

import androidx.annotation.StyleRes
import sgtmelon.scriptum.app.screen.view.AppActivity
import sgtmelon.scriptum.app.screen.vm.AppViewModel

/**
 * Интерфейс для обзения [AppViewModel] с [AppActivity]
 *
 * @author SerjantArbuz
 */
interface AppCallback {

    fun setTheme(@StyleRes resId: Int)

}