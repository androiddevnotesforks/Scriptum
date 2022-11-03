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
        val notesList = mockk<List<NoteItem>>()
        val binList = mockk<List<NoteItem>>()

        every { preferencesRepo.sort } returns sort
        coEvery {
            noteRepo.getList(sort, isBin = true, isOptimal = true, filterVisible = false)
        } returns binList

        runBlocking {
            assertEquals(useCase(isBin = true), binList)
        }

        coEvery {
            noteRepo.getList(sort, isBin = false, isOptimal = true, filterVisible = true)
        } returns notesList

        runBlocking {
            assertEquals(useCase(isBin = false), notesList)
        }

        coVerifySequence {
            preferencesRepo.sort
            noteRepo.getList(sort, isBin = true, isOptimal = true, filterVisible = false)

            preferencesRepo.sort
            noteRepo.getList(sort, isBin = false, isOptimal = true, filterVisible = true)
        }
    }
}