package sgtmelon.scriptum.presentation.screen.ui.callback.main

import androidx.annotation.IdRes
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.presentation.receiver.MainReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IMainViewModel

/**
 * Interface for communication [IMainViewModel] with [MainActivity].
 */
interface IMainActivity : IMainBridge,
    MainReceiver.BindCallback,
    MainReceiver.AlarmCallback {

    val openState: OpenState

    fun setupNavigation(@IdRes itemId: Int)

    fun setupInsets()


    fun onFabStateChange(state: Boolean)

    fun setFabState(state: Boolean)

    fun scrollTop(mainPage: MainPage)

    fun showPage(pageFrom: MainPage, pageTo: MainPage)

    fun openNoteScreen(noteType: NoteType)

}
