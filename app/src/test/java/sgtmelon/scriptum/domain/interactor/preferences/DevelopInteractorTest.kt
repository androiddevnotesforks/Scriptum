package sgtmelon.scriptum.domain.interactor.preferences

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.infrastructure.model.item.PrintItem
import sgtmelon.scriptum.infrastructure.model.item.PrintItem.Preference

/**
 * Test for [DevelopInteractorImpl].
 */
class DevelopInteractorTest : ParentTest() {

    @MockK lateinit var repository: DevelopRepo

    private val interactor by lazy { DevelopInteractorImpl(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun `getList for note`() {
        val type = PrintType.NOTE
        val list = mockk<List<PrintItem.Note>>()

        coEvery { repository.getPrintNoteList(isBin = false) } returns list

        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintNoteList(isBin = false)
        }
    }

    @Test fun `getList for bin`() {
        val type = PrintType.BIN
        val list = mockk<List<PrintItem.Note>>()

        coEvery { repository.getPrintNoteList(isBin = true) } returns list
        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintNoteList(isBin = true)
        }
    }

    @Test fun `getList for roll`() {
        val type = PrintType.ROLL
        val list = mockk<List<PrintItem.Roll>>()

        coEvery { repository.getPrintRollList() } returns list

        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintRollList()
        }
    }

    @Test fun `getList for visible`() {
        val type = PrintType.VISIBLE
        val list = mockk<List<PrintItem.Visible>>()

        coEvery { repository.getPrintVisibleList() } returns list

        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintVisibleList()
        }
    }

    @Test fun `getList for rank`() {
        val type = PrintType.RANK
        val list = mockk<List<PrintItem.Rank>>()

        coEvery { repository.getPrintRankList() } returns list

        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintRankList()
        }
    }

    @Test fun `getList for alarm`() {
        val type = PrintType.ALARM
        val list = mockk<List<PrintItem.Alarm>>()

        coEvery { repository.getPrintAlarmList() } returns list

        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintAlarmList()
        }
    }

    @Test fun `getList for key`() {
        val type = PrintType.KEY
        val list = mockk<List<Preference>>()

        coEvery { repository.getPrintPreferenceList() } returns list

        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintPreferenceList()
        }
    }

    @Test fun `getList for file`() {
        val type = PrintType.FILE
        val list = mockk<List<Preference>>()

        coEvery { repository.getPrintFileList() } returns list

        runBlocking {
            assertEquals(interactor.getList(type), list)
        }

        coVerifySequence {
            repository.getPrintFileList()
        }
    }

    @Test fun getRandomNoteId() {
        val id = Random.nextLong()

        coEvery { repository.getRandomNoteId() } returns id

        runBlocking {
            assertEquals(interactor.getRandomNoteId(), id)
        }

        coVerifySequence {
            repository.getRandomNoteId()
        }
    }

    @Test fun resetPreferences() {
        interactor.resetPreferences()

        verifySequence {
            repository.resetPreferences()
        }
    }
}