package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getRandomFutureTime
import sgtmelon.extension.getRandomPastTime
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainBridge
import kotlin.random.Random

/**
 * Test for [MainInteractor].
 */
@ExperimentalCoroutinesApi
class MainInteractorTest : ParentInteractorTest() {

    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var callback: IMainBridge

    private val interactor by lazy { MainInteractor(alarmRepo, callback) }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun tidyUpAlarm() = startCoTest {
        val itemList = MutableList(size = 2) {
            val id = it.toLong()
            val type = if (Random.nextBoolean()) NoteType.TEXT else NoteType.ROLL
            val date = if (it % 2 == 0) getRandomPastTime() else getRandomFutureTime()

            return@MutableList NotificationItem(
                    Note(id, name = "name_$it", color = it, type = type), Alarm(id, date)
            )
        }

        coEvery { alarmRepo.getList() } returns  null
        interactor.tidyUpAlarm()

        coEvery { alarmRepo.getList() } returns  itemList
        interactor.tidyUpAlarm()

        coVerifySequence {
            alarmRepo.getList()
            alarmRepo.getList()

            itemList.first().note.id.let {
                callback.cancelAlarm(it)
                alarmRepo.delete(it)
            }

            itemList.last().let {
                callback.setAlarm(it.alarm.date.getCalendar(), it.note.id)
            }
        }
    }

}