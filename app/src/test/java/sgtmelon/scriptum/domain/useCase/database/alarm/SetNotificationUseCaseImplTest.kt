package sgtmelon.scriptum.domain.useCase.database.alarm

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.test.common.nextString

/**
 * Test for [SetNotificationUseCaseImpl].
 */
class SetNotificationUseCaseImplTest : ParentTest() {

    @MockK lateinit var dataSource: AlarmDataSource
    @MockK lateinit var converter: AlarmConverter

    private val useCase by lazy { SetNotificationUseCaseImpl(dataSource, converter) }
    private val spyUseCase by lazy { spyk(useCase) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource, converter)
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
        val entity = mockk<AlarmEntity>()

        val date = nextString()
        val insertId = Random.nextLong()

        every { item.alarmDate = date } returns Unit
        every { item.alarmId = insertId } returns Unit

        every { converter.toEntity(item) } returns entity

        every { item.haveAlarm() } returns false
        coEvery { dataSource.insert(entity) } returns null

        runBlocking {
            useCase(item, date)
        }

        coEvery { dataSource.insert(entity) } returns insertId

        runBlocking {
            useCase(item, date)
        }

        every { item.haveAlarm() } returns true

        runBlocking {
            useCase(item, date)
        }

        coVerifySequence {
            item.alarmDate = date
            converter.toEntity(item)
            item.haveAlarm()
            dataSource.insert(entity)

            item.alarmDate = date
            converter.toEntity(item)
            item.haveAlarm()
            dataSource.insert(entity)
            item.alarmId = insertId

            item.alarmDate = date
            converter.toEntity(item)
            item.haveAlarm()
            dataSource.update(entity)
        }
    }
}