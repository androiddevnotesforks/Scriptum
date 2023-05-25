package sgtmelon.scriptum.infrastructure.screen.splash

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.main.MainViewModelImpl
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [SplashViewModelImpl].
 */
class SplashViewModelImplTest : ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var createNote: CreateNoteUseCase

    private val viewModel by lazy { SplashViewModelImpl(preferencesRepo, createNote) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, createNote)
    }

    @Test fun resetNotificationsHelp() {
        every { preferencesRepo.showNotificationsHelp = true } returns Unit

        viewModel.resetNotificationsHelp()

        verifySequence {
            preferencesRepo.showNotificationsHelp = true
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
}