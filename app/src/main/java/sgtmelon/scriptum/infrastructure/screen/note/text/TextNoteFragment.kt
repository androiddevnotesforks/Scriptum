package sgtmelon.scriptum.infrastructure.screen.note.text

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragment

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragmentImpl].
 */
interface TextNoteFragment : ParentNoteFragment<NoteItem.Text> {

    fun setupEnter(history: NoteHistory)

    fun onBindingLoad(isRankEmpty: Boolean)


    fun focusOnEdit(isCreate: Boolean)

    fun changeText(text: String, cursor: Int)

}