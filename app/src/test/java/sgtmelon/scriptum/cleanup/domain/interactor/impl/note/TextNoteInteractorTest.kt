package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentCoTest

/**
 * Test for [TextNoteInteractor].
 */
@ExperimentalCoroutinesApi
class TextNoteInteractorTest : ParentCoTest() {

    // TODO many items are common with [RollNoteInteractor]

    @MockK lateinit var noteRepo: NoteRepo

    private val interactor by lazy { TextNoteInteractor(noteRepo) }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteRepo)
    }

    @Test fun convertNote() = startCoTest {
        val item = mockk<NoteItem.Text>()

        coEvery { noteRepo.convertNote(item) } returns mockk()

        interactor.convertNote(item)

        coVerifySequence {
            noteRepo.convertNote(item)
        }
    }
}