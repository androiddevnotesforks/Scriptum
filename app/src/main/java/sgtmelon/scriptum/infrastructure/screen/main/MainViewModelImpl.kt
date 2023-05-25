package sgtmelon.scriptum.infrastructure.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class MainViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    private val createNote: CreateNoteUseCase
) : ViewModel(),
    MainViewModel {

    override var showNotificationsHelp: Boolean
        get() = preferencesRepo.showNotificationsHelp
        set(value) {
            preferencesRepo.showNotificationsHelp = value
        }

    override fun getNewNote(type: NoteType): NoteItem = createNote(type)

    override var previousPage: MainPage? = null

    override val currentPage: MutableLiveData<MainPage> = MutableLiveData(MainPage.NOTES)

    override val isFabPage: Boolean get() = currentPage.value == MainPage.NOTES

    override fun changePage(page: MainPage) {
        previousPage = currentPage.value
        currentPage.postValue(page)
    }
}