package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [SetNotificationUseCaseImpl].
 */
class SetNotificationUseCaseImplTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { SetNotificationUseCaseImpl(repository) }
    private val spyUseCase by lazy { spyk(useCase) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun `invoke via calendar`() {
        val item = mockk<NoteItem>()
        val calendar = mockk<Calendar>()
        val date = nextString()

        FastMock.timeExtension()
        every { calendar.getText() } returns date
        coEvery { spyUseCase(item, date) } returns Unit

        runBlocking {
            spyUseCase(item, calendar)
        }

        coVerifySequence {
            spyUseCase(item, calendar)

            calendar.getText()
            spyUseCase(item, date)
        }
    }

    @Test fun invoke() {
        val item = mockk<NoteItem>()
        val date = nextString()

        runBlocking {
            useCase(item, date)
        }

        coVerifySequence {
            repository.insertOrUpdate(item, date)
        }
    }
}