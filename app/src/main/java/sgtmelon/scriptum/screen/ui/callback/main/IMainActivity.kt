package sgtmelon.scriptum.screen.ui.callback.main

import androidx.annotation.IdRes
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.vm.main.MainViewModel

/**
 * Interface for communication [MainViewModel] with [MainActivity]
 */
interface IMainActivity {

    val openState: OpenState

    fun setupNavigation(@IdRes itemId: Int)

    fun changeFabState(state: Boolean)

    fun scrollTop(mainPage: MainPage)

    fun showPage(pageFrom: MainPage, pageTo: MainPage)

    fun startNoteActivity(noteType: NoteType)


    fun onCancelNoteBind(id: Long)

    fun onUpdateAlarm(id: Long)

}
