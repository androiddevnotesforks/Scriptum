package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmBridge
import kotlin.random.Random

/**
 * Test for [AlarmInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IAlarmBridge

    private val interactor by lazy {
        AlarmInteractor(preferenceRepo, alarmRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getRepeat() = FastTest.getRepeat(preferenceRepo) { interactor.repeat }

    @Test fun getVolume() = FastTest.getVolume(preferenceRepo) { interactor.volume }

    @Test fun getVolumeIncrease() {
        fun checkRequestGet(value: Boolean) {
            every { preferenceRepo.volumeIncrease } returns value
            assertEquals(interactor.volumeIncrease, value)
        }

        val valueList = listOf(true, false)
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.volumeIncrease }
        }
    }

    @Test fun getModel() = startCoTest {
        val noteId = Random.nextLong()
        val item = data.itemList.random()

        coEvery { noteRepo.getItem(noteId, optimisation = true) } returns null
        assertEquals(null, interactor.getModel(noteId))

        coEvery { noteRepo.getItem(noteId, optimisation = true) } returns item
        assertEquals(item, interactor.getModel(noteId))

        coVerifySequence {
            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, optimisation = true)

            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, optimisation = true)
        }
    }

    @Test fun setupRepeat() {
        TODO()
    }

}