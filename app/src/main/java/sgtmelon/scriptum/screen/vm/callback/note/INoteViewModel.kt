package sgtmelon.scriptum.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Interface for communication [NoteActivity] with [NoteViewModel]
 */
interface INoteViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onSetupFragment(checkCache: Boolean)

    fun onPressBack(): Boolean

    fun onUpdateNoteId(id: Long)

    fun onUpdateNoteColor(@Color color: Int)

    fun onConvertNote()

}