package sgtmelon.scriptum.infrastructure.screen.splash

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class SplashViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    private val createNote: CreateNoteUseCase
) : ViewModel(),
    SplashViewModel {

    override fun resetNotificationsHelp() {
        preferencesRepo.showNotificationsHelp = true
    }

    override fun getNewNote(type: NoteType): NoteItem = createNote(type)

}