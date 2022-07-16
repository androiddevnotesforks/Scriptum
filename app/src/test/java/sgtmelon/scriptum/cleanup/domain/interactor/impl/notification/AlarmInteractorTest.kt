package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.common.utils.*
import sgtmelon.scriptum.*
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.parent.ParentInteractorTest
import java.util.*
import kotlin.random.Random

/**
 * Test for [AlarmInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferences: Preferences
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var noteRepo: INoteRepo

    private val interactor by lazy {
        AlarmInteractor(preferences, alarmRepo, noteRepo)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences, alarmRepo, noteRepo)
    }


    @Test fun getRepeat() = FastTest.getRepeat(preferences) { interactor.repeat }

    @Test fun getVolume() = FastTest.getVolume(preferences) { interactor.volume }

    @Test fun getVolumeIncrease() {
        val value = Random.nextBoolean()

        every { preferences.isVolumeIncrease } returns value
        assertEquals(value, interactor.isVolumeIncrease)

        verifySequence {
            preferences.isVolumeIncrease
        }
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
        val size = getRandomSize()
        val valueArray = IntArray(size) { Random.nextInt() }
        val repeat = valueArray.indices.random()
        val minute = valueArray[repeat]

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
            val type = if (Random.nextBoolean()) NoteType.TEXT else NoteType.ROLL

            return@MutableList NotificationItem(
                    Note(id, name = "name_$it", color = it, type = type), Alarm(id, dateList[it])
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