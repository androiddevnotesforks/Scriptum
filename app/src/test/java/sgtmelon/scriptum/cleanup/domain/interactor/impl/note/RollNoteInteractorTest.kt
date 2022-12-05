package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentCoTest

/**
 * Test for [RollNoteInteractor].
 */
@ExperimentalCoroutinesApi
class RollNoteInteractorTest : ParentCoTest() {

    // TODO many items are common with [TextNoteInteractor]

    @MockK lateinit var noteRepo: NoteRepo

    private val interactor by lazy { RollNoteInteractor(noteRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteRepo)
    }

    @Test fun convertNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()

        coEvery { noteRepo.convertNote(item, useCache = true) } returns mockk()

        interactor.convertNote(item)

        coVerifySequence {
            noteRepo.convertNote(item, useCache = true)
        }
    }
}