package sgtmelon.scriptum.cleanup.domain.interactor.callback.note

import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel

/**
 * Interface for communication [ITextNoteViewModel] with [TextNoteInteractor].
 */
interface ITextNoteInteractor : IParentNoteInteractor<NoteItem.Text>