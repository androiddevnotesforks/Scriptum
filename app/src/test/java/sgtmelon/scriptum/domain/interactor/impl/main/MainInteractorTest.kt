package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainBridge

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


    @Test fun tidyUpAlarm() {
        TODO()
    }

}