package sgtmelon.scriptum.infrastructure.screen.main

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

interface MainViewModel {

    val showNotificationsHelp: Boolean

    fun hideNotificationsHelp()

    fun getNewNote(type: NoteType): NoteItem

    val previousPage: MainPage?

    val currentPage: LiveData<MainPage>

    val isFabPage: Boolean

    fun changePage(page: MainPage)

}