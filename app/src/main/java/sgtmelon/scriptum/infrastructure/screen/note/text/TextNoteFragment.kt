package sgtmelon.scriptum.infrastructure.screen.note.text

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.INoteHistory
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragment

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragmentImpl].
 */
interface TextNoteFragment : ParentNoteFragment<NoteItem.Text> {

    fun setupEnter(history: INoteHistory)

    fun onBindingLoad(isRankEmpty: Boolean)


    fun focusOnEdit(isCreate: Boolean)

    fun changeText(text: String, cursor: Int)

}