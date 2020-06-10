package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteInteractor].
 */
interface ITextNoteInteractor : IParentNoteInteractor<NoteItem.Text>