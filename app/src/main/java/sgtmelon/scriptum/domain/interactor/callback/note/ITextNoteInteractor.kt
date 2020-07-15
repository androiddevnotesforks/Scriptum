package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel

/**
 * Interface for communication [ITextNoteViewModel] with [TextNoteInteractor].
 */
interface ITextNoteInteractor : IParentNoteInteractor<NoteItem.Text>