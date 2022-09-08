package sgtmelon.scriptum.domain.useCase.alarm

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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.test.common.nextLongOrNull
import sgtmelon.test.common.nextString

/**
 * Test for [SetNotificationUseCaseImpl].
 */
class SetNotificationUseCaseImplTest : ParentTest() {

    @MockK lateinit var noteRepo: NoteRepo
    @MockK lateinit var alarmRepo: AlarmRepo

    private val useCase by lazy { SetNotificationUseCaseImpl(noteRepo, alarmRepo) }
    private val spyUseCase by lazy { spyk(useCase) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteRepo, alarmRepo)
    }

    @Test fun `invoke via notificationItem`() {
        val item = mockk<NotificationItem>()
        val note = mockk<NotificationItem.Note>()
        val noteId = Random.nextLong()
        val noteItem = mockk<NoteItem>()
        val alarm = mockk<NotificationItem.Alarm>()
        val date = nextString()
        val newId = Random.nextLong()

        val newItem = mockk<NotificationItem>()
        val newAlarm = mockk<NotificationItem.Alarm>()

        every { item.note } returns note
        every { note.id } returns noteId
        coEvery { noteRepo.getItem(noteId, isOptimal = true) } returns null

        runBlocking {
            assertNull(useCase(item))
        }

        coEvery { noteRepo.getItem(noteId, isOptimal = true) } returns noteItem
        every { item.alarm } returns alarm
        every { alarm.date } returns date
        coEvery { alarmRepo.insertOrUpdate(noteItem, date) } returns null

        runBlocking {
            assertNull(useCase(item))
        }

        coEvery { alarmRepo.insertOrUpdate(noteItem, date) } returns newId
        every { alarm.copy(id = newId) } returns newAlarm
        every { item.copy(alarm = newAlarm) } returns newItem

        runBlocking {
            assertEquals(useCase(item), newItem)
        }

        @Suppress("UnusedDataClassCopyResult")
        coVerifySequence {
            item.note
            note.id
            noteRepo.getItem(noteId, isOptimal = true)

            item.note
            note.id
            noteRepo.getItem(noteId, isOptimal = true)
            item.alarm
            alarm.date
            alarmRepo.insertOrUpdate(noteItem, date)

            item.note
            note.id
            noteRepo.getItem(noteId, isOptimal = true)
            item.alarm
            alarm.date
            alarmRepo.insertOrUpdate(noteItem, date)
            item.alarm
            alarm.copy(id = newId)
            item.copy(alarm = newAlarm)
        }
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

        coEvery { alarmRepo.insertOrUpdate(item, date) } returns nextLongOrNull()

        runBlocking {
            useCase(item, date)
        }

        coVerifySequence {
            alarmRepo.insertOrUpdate(item, date)
        }
    }
}