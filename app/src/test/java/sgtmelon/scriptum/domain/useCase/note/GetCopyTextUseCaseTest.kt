package sgtmelon.scriptum.domain.useCase.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.utils.extensions.note.joinToText
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [GetCopyTextUseCase].
 */
class GetCopyTextUseCaseTest : ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { GetCopyTextUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun `getCopyText for text note`() {
        val item = mockk<NoteItem.Text>()

        val name = nextString()
        val text = nextString()

        every { item.name } returns ""
        every { item.text } returns text

        runBlocking {
            assertEquals(useCase(item), text)
        }

        every { item.name } returns name

        runBlocking {
            assertEquals(useCase(item), "$name\n$text")
        }

        verifySequence {
            item.name
            item.text

            item.name
            item.name
            item.text
        }
    }

    @Test fun `getCopyText for roll note`() {
        val item = mockk<NoteItem.Roll>()

        val id = Random.nextLong()
        val name = nextString()
        val list = mockk<MutableList<RollItem>>()
        val text = nextString()

        FastMock.rollExtensions()

        every { item.name } returns ""
        every { item.id } returns id
        coEvery { repository.getRollList(id) } returns list
        every { list.joinToText() } returns text

        runBlocking {
            assertEquals(useCase(item), text)
        }

        every { item.name } returns name

        runBlocking {
            assertEquals(useCase(item), "$name\n$text")
        }

        coVerifySequence {
            item.name
            item.id
            repository.getRollList(id)
            list.joinToText()

            item.name
            item.name
            item.id
            repository.getRollList(id)
            list.joinToText()
        }
    }
}
