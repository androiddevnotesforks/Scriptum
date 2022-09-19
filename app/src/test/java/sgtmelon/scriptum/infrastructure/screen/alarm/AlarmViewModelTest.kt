package sgtmelon.scriptum.infrastructure.screen.alarm

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentCoTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase

/**
 * Test for [AlarmViewModel].
 */
@ExperimentalCoroutinesApi
class AlarmViewModelTest : ParentCoTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var noteRepo: NoteRepo
    @MockK lateinit var getMelodyList: GetMelodyListUseCase
    @MockK lateinit var setNotification: SetNotificationUseCase
    @MockK lateinit var deleteNotification: DeleteNotificationUseCase
    @MockK lateinit var shiftDateIfExist: ShiftDateIfExistUseCase

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy {
        AlarmViewModel(
            preferencesRepo, noteRepo, getMelodyList, setNotification, deleteNotification,
            shiftDateIfExist
        )
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            preferencesRepo, noteRepo, getMelodyList, setNotification, deleteNotification,
            shiftDateIfExist
        )
    }

    //endregion

    @Test fun todo() {
        TODO()
    }

    @Test fun onReceiveUnbindNote() {
        viewModel.onReceiveUnbindNote(Random.nextLong())

        val noteItem = mockk<NoteItem>()
        val noteId = Random.nextLong()
        val fakeNoteId = Random.nextLong()

        assertNotEquals(noteId, fakeNoteId)

        viewModel.noteItem.value = noteItem

        every { noteItem.isStatus } returns false
        viewModel.onReceiveUnbindNote(fakeNoteId)

        every { noteItem.isStatus } returns true
        every { noteItem.id } returns noteId
        viewModel.onReceiveUnbindNote(fakeNoteId)

        viewModel.onReceiveUnbindNote(noteId)

        verifySequence {
            noteItem.isStatus

            noteItem.isStatus
            noteItem.id

            noteItem.isStatus
            noteItem.id
            noteItem.isStatus = false
        }
    }
}