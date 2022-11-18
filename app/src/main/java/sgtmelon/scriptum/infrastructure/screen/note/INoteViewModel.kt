package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

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