package sgtmelon.scriptum.screen.callback.main

import androidx.annotation.IdRes
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.screen.vm.main.MainViewModel

/**
 * Интерфейс для общения [MainViewModel] с [MainActivity]
 *
 * @author SerjantArbuz
 */
interface MainCallback {

    fun setupNavigation(@IdRes itemId: Int)

    fun changeFabState(state: Boolean)

    fun scrollTop(mainPage: MainPage)

    fun showPage(pageFrom: MainPage, pageTo: MainPage)

    fun onCancelNoteBind(id: Long)

}
