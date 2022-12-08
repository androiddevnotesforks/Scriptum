package sgtmelon.scriptum.infrastructure.screen.main.notes

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import java.util.Calendar
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNotesListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [NotesViewModelImpl].
 */
class NotesViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo

    @MockK lateinit var getList: GetNotesListUseCase
    @MockK lateinit var sortList: SortNoteListUseCase
    @MockK lateinit var getCopyText: GetCopyTextUseCase
    @MockK lateinit var convertNote: ConvertNoteUseCase
    @MockK lateinit var updateNote: UpdateNoteUseCase
    @MockK lateinit var deleteNote: DeleteNoteUseCase
    @MockK lateinit var setNotification: SetNotificationUseCase
    @MockK lateinit var deleteNotification: DeleteNotificationUseCase
    @MockK lateinit var getNotificationDateList: GetNotificationDateListUseCase

    @MockK lateinit var calendar: Calendar

    private val viewModel by lazy {
        NotesViewModelImpl(
            preferencesRepo, getList, sortList, getCopyText, convertNote, updateNote, deleteNote,
            setNotification, deleteNotification, getNotificationDateList
        )
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            preferencesRepo, calendar,
            getList, sortList, getCopyText, convertNote, updateNote, deleteNote,
            setNotification, deleteNotification, getNotificationDateList
        )
    }

    //endregion

    @Test fun getShowList() {
        TODO()
    }

    @Test fun getListHide() {
        TODO()
    }

    @Test fun getItemList() {
        TODO()
    }

    @Test fun updateData() {
        TODO()
    }

    @Test fun getNoteNotification() {
        TODO()
    }

    @Test fun getOccupiedDateList() {
        TODO()
    }

    @Test fun deleteNoteNotification() {
        TODO()
    }

    @Test fun setNoteNotification() {
        TODO()
    }

    @Test fun updateNoteBind() {
        TODO()
    }

    @Test fun convertNote() {
        TODO()
    }

    @Test fun getNoteText() {
        TODO()
    }

    @Test fun deleteNote() {
        TODO()
    }
}