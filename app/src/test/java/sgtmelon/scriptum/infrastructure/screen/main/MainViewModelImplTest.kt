package sgtmelon.scriptum.infrastructure.screen.main

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.testing.getOrAwaitValue
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest
import kotlin.random.Random

/**
 * Test for [MainViewModelImpl].
 */
class MainViewModelImplTest : ParentLiveDataTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var createNote: CreateNoteUseCase

    private val viewModel by lazy { MainViewModelImpl(preferencesRepo, createNote) }

    @Before override fun setUp() {
        super.setUp()

        assertNull(viewModel.previousPage)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.NOTES)
        assertTrue(viewModel.isFabPage)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, createNote)
    }

    @Test fun isShowNotificationsHelp() {
        val value = Random.nextBoolean()

        every { preferencesRepo.showNotificationsHelp } returns value

        assertEquals(viewModel.showNotificationsHelp, value)

        verifySequence {
            preferencesRepo.showNotificationsHelp
        }
    }

    @Test fun setShowNotificationsHelp() {
        val value = Random.nextBoolean()

        every { preferencesRepo.showNotificationsHelp = value } returns Unit

        viewModel.showNotificationsHelp = value

        verifySequence {
            preferencesRepo.showNotificationsHelp = value
        }
    }

    @Test fun getNewNote() {
        val type = mockk<NoteType>()
        val item = mockk<NoteItem>()

        every { createNote(type) } returns item

        assertEquals(viewModel.getNewNote(type), item)

        verifySequence {
            createNote(type)
        }
    }

    @Test fun changePage() {
        viewModel.changePage(MainPage.RANK)
        assertEquals(viewModel.previousPage, MainPage.NOTES)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.RANK)
        assertFalse(viewModel.isFabPage)

        viewModel.changePage(MainPage.NOTES)
        assertEquals(viewModel.previousPage, MainPage.RANK)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.NOTES)
        assertTrue(viewModel.isFabPage)

        viewModel.changePage(MainPage.BIN)
        assertEquals(viewModel.previousPage, MainPage.NOTES)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.BIN)
        assertFalse(viewModel.isFabPage)
    }
}