package sgtmelon.scriptum.app.screen.callback.main

import androidx.annotation.IdRes
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.screen.view.main.MainActivity
import sgtmelon.scriptum.app.screen.vm.main.MainViewModel

/**
 * Интерфейс для общения [MainViewModel] с [MainActivity]
 *
 * @author SerjantArbuz
 */
interface MainCallback {

    fun setupNavigation(@IdRes itemId: Int)

    fun changeFabState(state: Boolean)

    fun scrollTop(name: MainPage.Name)

    fun showPage(pageFrom: MainPage.Name, pageTo: MainPage.Name)

}
