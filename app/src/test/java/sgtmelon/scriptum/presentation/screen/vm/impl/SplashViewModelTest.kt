package sgtmelon.scriptum.presentation.screen.vm.impl

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity

/**
 * Test for [SplashViewModel].
 */
@ExperimentalCoroutinesApi
class SplashViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: ISplashActivity

    @MockK lateinit var interactor: ISplashInteractor

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { SplashViewModel(application) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, bundle)
    }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup_introStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns null
        every { interactor.firstStart } returns true

        viewModel.onSetup(bundle = null)
        viewModel.onSetup(bundle)

        verifySequence {
            interactor.firstStart
            callback.openIntroScreen()

            interactor.firstStart
            callback.openIntroScreen()
        }
    }

    @Test fun onSetup_mainStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns null
        every { interactor.firstStart } returns false

        viewModel.onSetup(bundle = null)
        viewModel.onSetup(bundle)

        verifySequence {
            interactor.firstStart
            callback.openMainScreen()

            interactor.firstStart
            callback.openMainScreen()
        }
    }

    @Test fun onSetup_alarmStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.ALARM
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns ID

        viewModel.onSetup(bundle)

        verifySequence { callback.openAlarmScreen(ID) }
    }

    @Test fun onSetup_bindStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.BIND
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns ID
        every { bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR) } returns COLOR
        every { bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE) } returns TYPE

        viewModel.onSetup(bundle)

        verifySequence { callback.openNoteScreen(ID, COLOR, TYPE) }
    }

    @Test fun onSetup_notificationStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.INFO

        viewModel.onSetup(bundle)

        verifySequence { callback.openNotificationScreen() }
    }


    companion object {
        private const val ID = 33L
        private const val COLOR = 5
        private const val TYPE = 0
    }

}