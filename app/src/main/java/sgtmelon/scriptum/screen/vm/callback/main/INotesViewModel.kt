package sgtmelon.scriptum.screen.vm.callback.main

import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Interface for communication [NotesFragment] with [NotesViewModel]
 *
 * @author SerjantArbuz
 */
interface INotesViewModel : IParentViewModel {

    fun onUpdateData()

    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)

    fun onResultOptionsDialog(p: Int, which: Int)

    fun onCancelNoteBind(id: Long)

}