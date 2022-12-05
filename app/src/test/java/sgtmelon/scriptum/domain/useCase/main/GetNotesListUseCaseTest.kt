package sgtmelon.scriptum.domain.useCase.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [GetNotesListUseCase].
 */
class GetNotesListUseCaseTest : ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var noteRepo: NoteRepo

    private val useCase by lazy { GetNotesListUseCase(preferencesRepo, noteRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, noteRepo)
    }

    @Test fun invoke() {
        val sort = mockk<Sort>()
        val pair = mockk<Pair<List<NoteItem>, Boolean>>()

        every { preferencesRepo.sort } returns sort
        coEvery { noteRepo.getNotesList(sort) } returns pair

        runBlocking {
            assertEquals(useCase(), pair)
        }

        coVerifySequence {
            preferencesRepo.sort
            noteRepo.getNotesList(sort)
        }
    }
}