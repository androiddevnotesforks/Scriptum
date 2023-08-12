package sgtmelon.scriptum.domain.useCase.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.tests.uniter.ParentTest
import kotlin.random.Random

/**
 * Test for [SaveNoteUseCase].
 */
class SaveNoteUseCaseTest : ParentTest() {

    @MockK lateinit var noteRepo: NoteRepo
    @MockK lateinit var rankRepo: RankRepo

    private val useCase by lazy { SaveNoteUseCase(noteRepo, rankRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteRepo, rankRepo)
    }

    @Test fun `invoke for text`() = startTest(mockk<NoteItem.Text>()) { item, isCreate ->
        noteRepo.saveNote(item, isCreate)
    }

    @Test fun `invoke for roll`() = startTest(mockk<NoteItem.Roll>()) { item, isCreate ->
        noteRepo.saveNote(item, isCreate)
    }

    private inline fun <T : NoteItem> startTest(
        item: T,
        crossinline onSave: suspend (item: T, isCreate: Boolean) -> Unit
    ) {
        val isCreate = Random.nextBoolean()

        coEvery { onSave(item, isCreate) } returns Unit
        coEvery { rankRepo.updateConnection(item) } returns Unit

        runBlocking {
            useCase(item, isCreate)
        }

        coVerifySequence {
            onSave(item, isCreate)
            rankRepo.updateConnection(item)
        }
    }
}