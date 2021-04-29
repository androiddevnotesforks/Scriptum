package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.*
import sgtmelon.scriptum.*
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.domain.model.key.NoteType
import java.util.*
import kotlin.random.Random

/**
 * Test for [AlarmInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var noteRepo: INoteRepo
//    @MockK lateinit var callback: IAlarmBridge

    private val interactor by lazy {
        AlarmInteractor(preferenceRepo, alarmRepo, noteRepo/*, callback*/)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, alarmRepo, noteRepo/*, callback*/)
    }


    @Test fun getRepeat() = FastTest.getRepeat(preferenceRepo) { interactor.repeat }

    @Test fun getVolume() = FastTest.getVolume(preferenceRepo) { interactor.volume }

    @Test fun getVolumeIncrease() {
        val value = Random.nextBoolean()

        every { preferenceRepo.volumeIncrease } returns value
        assertEquals(value, interactor.volumeIncrease)

        verifySequence {
            preferenceRepo.volumeIncrease
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