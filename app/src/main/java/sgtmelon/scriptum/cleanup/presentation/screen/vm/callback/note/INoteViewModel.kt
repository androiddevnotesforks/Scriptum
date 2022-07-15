package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.NoteViewModel

/**
 * Interface for communication [INoteActivity] with [NoteViewModel].
 */
interface INoteViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onSetupFragment(checkCache: Boolean)

    fun onPressBack(): Boolean

    fun onUpdateNoteId(id: Long)

    fun onUpdateNoteColor(@Color color: Int)

    fun onConvertNote()

}