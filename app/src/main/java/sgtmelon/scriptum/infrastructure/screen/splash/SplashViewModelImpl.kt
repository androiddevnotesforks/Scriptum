package sgtmelon.scriptum.infrastructure.screen.splash

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class SplashViewModelImpl(
    private val createNote: CreateNoteUseCase
) : ViewModel(),
    SplashViewModel {

    override fun getNewNote(type: NoteType): NoteItem = createNote(type)

}