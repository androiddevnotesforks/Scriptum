package sgtmelon.scriptum.domain.useCase.bind

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

/**
 * Test for [GetBindNoteListUseCase].
 */
class GetBindNoteListUseCaseTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var noteRepo: NoteRepo

    private val useCase by lazy { GetBindNoteListUseCase(preferencesRepo, noteRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, noteRepo)
    }

    @Test fun invoke() {
        val sort = mockk<Sort>()
        val list = mockk<List<NoteItem>>()

        every { preferencesRepo.sort } returns sort
        coEvery { noteRepo.getBindNoteList(sort) } returns list

        runBlocking {
            assertEquals(list, useCase())
        }

        coVerifySequence {
            preferencesRepo.sort
            noteRepo.getBindNoteList(sort)
        }
    }
}
