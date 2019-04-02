package sgtmelon.scriptum.app.screen.app

import androidx.annotation.StyleRes

/**
 * Интерфейс для обзения [AppViewModel] с [AppActivity]
 *
 * @author SerjantArbuz
 */
interface AppCallback {

    fun setTheme(@StyleRes resId: Int)

}