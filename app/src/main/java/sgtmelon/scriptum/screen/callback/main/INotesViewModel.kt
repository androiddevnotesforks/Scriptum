package sgtmelon.scriptum.screen.callback.main

import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Интерфейс для общения [NotesFragment] с [NotesViewModel]
 *
 * @author SerjantArbuz
 */
interface INotesViewModel {

    fun onSetup()

    fun onUpdateData()

    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)

    fun onResultOptionsDialog(p: Int, which: Int)

    fun onCancelNoteBind(id: Long)

}