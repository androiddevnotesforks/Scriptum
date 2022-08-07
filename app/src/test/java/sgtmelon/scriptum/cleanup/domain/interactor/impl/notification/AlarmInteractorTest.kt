package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.clearSeconds
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getNewCalendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.test.common.nextString

/**
 * Test for [AlarmInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmInteractorTest : ParentInteractorTest() {

    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var noteRepo: INoteRepo

    private val interactor by lazy {
        AlarmInteractor(alarmRepo, noteRepo)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(alarmRepo, noteRepo)
    }

    @Test fun getModel() = startCoTest {
        val noteId = Random.nextLong()
        val item = mockk<NoteItem>()

        coEvery { noteRepo.getItem(noteId, isOptimal = true) } returns null
        assertEquals(null, interactor.getModel(noteId))

        coEvery { noteRepo.getItem(noteId, isOptimal = true) } returns item
        assertEquals(item, interactor.getModel(noteId))

        coVerifySequence {
            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, isOptimal = true)

            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, isOptimal = true)
        }
    }

    @Test fun setupRepeat() = startCoTest {
        val item = mockk<NoteItem>()
        val valueArray = IntArray(Repeat.values().size) { Random.nextInt() }
        val repeat = Repeat.values().random()
        val minute = valueArray[repeat.ordinal]

        val calendar = mockk<Calendar>()
        val calendarText = nextString()
        val id = Random.nextLong()

        coEvery { spyInteractor.checkDateExist(calendar) } returns Unit

        FastMock.timeExtension()
        every { getCalendarWithAdd(minute) } returns calendar
        every { calendar.getText() } returns calendarText
        every { item.id } returns id

        assertNull(spyInteractor.setupRepeat(item, intArrayOf(), repeat))
        assertEquals(calendar, spyInteractor.setupRepeat(item, valueArray, repeat))

        coVerifySequence {
            spyInteractor.setupRepeat(item, intArrayOf(), repeat)

            spyInteractor.setupRepeat(item, valueArray, repeat)
            getCalendarWithAdd(minute)
            spyInteractor.checkDateExist(calendar)
            calendar.getText()
            alarmRepo.insertOrUpdate(item, calendarText)
        }
    }

    @Test fun checkDateExist() = startCoTest {
        val dateList = List(size = 3) { getCalendarWithAdd(it).getText() }

        val itemList = MutableList(dateList.size) {
            val id = it.toLong()
            val color = Color.values().random()
            val type = NoteType.values().random()

            return@MutableList NotificationItem(
                    Note(id, name = "name_$it", color = color, type = type), Alarm(id, dateList[it])
            )
        }

        val currentCalendar = getNewCalendar().clearSeconds()
        val minute = currentCalendar.get(Calendar.MINUTE)

        coEvery { alarmRepo.getList() } returns itemList
        interactor.checkDateExist(currentCalendar)

        /**
         * Get error on last hour minutes.
         */
        val expected = (minute + dateList.size) % 60
        assertEquals(expected, currentCalendar.get(Calendar.MINUTE))

        coVerifySequence {
            alarmRepo.getList()
        }
    }
}