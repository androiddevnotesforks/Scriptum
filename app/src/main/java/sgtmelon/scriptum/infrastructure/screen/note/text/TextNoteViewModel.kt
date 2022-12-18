package sgtmelon.scriptum.infrastructure.screen.note.text

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModel

/**
 * Interface for communication [TextNoteFragment] with [TextNoteViewModelImpl].
 */
interface TextNoteViewModel : ParentNoteViewModel<NoteItem.Text>