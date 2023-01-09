package sgtmelon.scriptum.infrastructure.screen.note

import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteViewModelImpl

/**
 * Interface for communication dataBinding with [TextNoteViewModelImpl]/[RollNoteViewModelImpl].
 */
interface NoteMenu {

    // Inside bin

    fun restore(): Flow<Unit>

    fun onMenuRestoreOpen()

    fun deleteForever(): Flow<Unit>


    // Edit mode

    fun onMenuUndo()

    fun onMenuRedo()

    /**
     * Return true on success save
     * [changeMode] - need change mode or not.
     */
    fun onMenuSave(changeMode: Boolean): Boolean

    // Read mode

    fun switchBind(): Flow<Unit>

    fun delete(): Flow<NoteItem>

    fun onMenuEdit()

}