package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main

import androidx.annotation.IdRes
import sgtmelon.scriptum.cleanup.domain.model.key.MainPage
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.MainScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

/**
 * Interface for communication [IMainViewModel] with [MainActivity].
 */
interface IMainActivity : MainScreenReceiver.AlarmCallback,
    UnbindNoteReceiver.Callback {

    val openState: OpenState

    fun setupNavigation(@IdRes itemId: Int)

    fun setupInsets()


    fun onFabStateChange(isVisible: Boolean, withGap: Boolean)

    fun changeFabVisible(isVisible: Boolean, withGap: Boolean)

    fun scrollTop(mainPage: MainPage)

    fun showPage(pageFrom: MainPage, pageTo: MainPage)

    fun openNoteScreen(noteType: NoteType)

}
