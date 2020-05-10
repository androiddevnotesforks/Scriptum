package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationBridge
import kotlin.random.Random

/**
 * Test for [NotificationInteractor].
 */
@ExperimentalCoroutinesApi
class NotificationInteractorTest : ParentInteractorTest() {

    private val data = TestData.Notification

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var bindRepo: IBindRepo
    @MockK lateinit var callback: INotificationBridge

    private val interactor by lazy {
        NotificationInteractor(preferenceRepo, alarmRepo, bindRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getCount() = startCoTest {
        val countList = listOf(null, Random.nextInt(), Random.nextInt(), null)

        countList.forEach {
            coEvery { bindRepo.getNotificationCount() } returns it
            assertEquals(it, interactor.getCount())
        }

        coVerifySequence {
            repeat(countList.size) { bindRepo.getNotificationCount() }
        }
    }

    @Test fun getList() = startCoTest {
        val list = data.itemList

        coEvery { alarmRepo.getList() } returns null
        assertEquals(null, interactor.getList())

        coEvery { alarmRepo.getList() } returns list
        assertEquals(list, interactor.getList())

        coVerifySequence {
            alarmRepo.getList()
            alarmRepo.getList()
        }
    }

    @Test fun cancelNotification() = startCoTest {
        val item = data.itemList.random()
        val id = item.note.id

        interactor.cancelNotification(item)

        coVerifySequence {
            alarmRepo.delete(id)
            callback.cancelAlarm(id)
        }
    }

}