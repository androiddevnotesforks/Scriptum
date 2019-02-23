package sgtmelon.scriptum.app.screen.parent

import androidx.annotation.StyleRes

/**
 * Интерфейс для обзения [ParentViewModel] с [ParentActivity]
 */
interface ParentCallback {

    fun setTheme(@StyleRes resId: Int)

}