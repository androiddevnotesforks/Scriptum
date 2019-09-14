package sgtmelon.scriptum.screen.ui.callback.main

import android.content.Intent
import androidx.annotation.IdRes
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.vm.main.MainViewModel

/**
 * Interface for communication [MainViewModel] with [MainActivity]
 */
interface IMainActivity {

    fun setupNavigation(@IdRes itemId: Int)

    fun changeFabState(state: Boolean)

    fun scrollTop(mainPage: MainPage)

    fun showPage(pageFrom: MainPage, pageTo: MainPage)

    fun onCancelNoteBind(id: Long)

    fun startActivity(intent: Intent)

}
