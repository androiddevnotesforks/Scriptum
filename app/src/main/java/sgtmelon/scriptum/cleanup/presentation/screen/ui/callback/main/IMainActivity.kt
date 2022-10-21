package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main

import androidx.annotation.IdRes
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

/**
 * Interface for communication [IMainViewModel] with [MainActivity].
 */
interface IMainActivity : UnbindNoteReceiver.Callback {

    fun setupNavigation(@IdRes itemId: Int)

    fun setupInsets()


    fun onFabStateChange(isVisible: Boolean, withGap: Boolean)

    fun changeFabVisible(isVisible: Boolean, withGap: Boolean)

    fun scrollTop(mainPage: MainPage)

    fun showPage(pageFrom: MainPage, pageTo: MainPage)

}
