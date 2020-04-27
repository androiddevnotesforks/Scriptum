package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.domain.model.annotation.Theme
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


    @Test fun getTheme() {
        every { preferenceRepo.theme } returns Theme.DARK
        assertEquals(Theme.DARK, interactor.theme)

        every { preferenceRepo.theme } returns Theme.LIGHT
        assertEquals(Theme.LIGHT, interactor.theme)

        verifySequence {
            interactor.theme
            interactor.theme
        }
    }

    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { bindRepo.getNotificationCount() } returns count

        assertEquals(count, interactor.getCount())

        coVerifySequence {
            bindRepo.getNotificationCount()
        }
    }

    @Test fun getList() = startCoTest {
        val list = data.itemList

        coEvery { alarmRepo.getList() } returns list

        assertEquals(list, interactor.getList())

        coVerifySequence {
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