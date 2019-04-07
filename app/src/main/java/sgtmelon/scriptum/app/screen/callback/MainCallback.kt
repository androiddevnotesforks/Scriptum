package sgtmelon.scriptum.app.screen.callback

import androidx.annotation.IdRes
import sgtmelon.scriptum.app.model.key.MainPage

/**
 * Интерфейс для общения [MainViewModel] с [MainActivity]
 */
interface MainCallback {

    fun setupNavigation(@IdRes itemId: Int)

    fun changeFabState(state: Boolean)

    fun scrollTop(name: MainPage.Name)

    fun showPage(pageFrom: MainPage.Name, pageTo: MainPage.Name)

}
