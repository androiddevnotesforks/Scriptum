package sgtmelon.scriptum.app.view.callback

import sgtmelon.scriptum.app.view.activity.MainActivity

/**
 * Интерфейс для общения с [MainActivity]
 */
interface MainCallback {

    fun changeFabState(show: Boolean)

}
