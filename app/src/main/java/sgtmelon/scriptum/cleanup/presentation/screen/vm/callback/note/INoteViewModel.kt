package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.NoteViewModel
import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Interface for communication [INoteActivity] with [NoteViewModel].
 */
interface INoteViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onSetupFragment(checkCache: Boolean)

    fun onPressBack(): Boolean

    fun onUpdateNoteId(id: Long)

    fun onUpdateNoteColor(color: Color)

    fun onConvertNote()

}