package sgtmelon.scriptum.presentation.screen.vm.impl

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity

/**
 * Test for [SplashViewModel].
 */
@ExperimentalCoroutinesApi
class SplashViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: ISplashActivity

    @MockK lateinit var interactor: ISplashInteractor

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { SplashViewModel(callback, interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, bundle)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup_introStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns null
        every { interactor.firstStart } returns true

        viewModel.onSetup(bundle = null)
        viewModel.onSetup(bundle)

        verifySequence {
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            interactor.firstStart
            callback.openIntroScreen()

            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            bundle.getString(OpenFrom.INTENT_KEY)
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
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            interactor.firstStart
            callback.openMainScreen()

            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            bundle.getString(OpenFrom.INTENT_KEY)
            interactor.firstStart
            callback.openMainScreen()
        }
    }

    @Test fun onSetup_alarmStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.ALARM
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns ID

        viewModel.onSetup(bundle)

        verifySequence {
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            bundle.getString(OpenFrom.INTENT_KEY)
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            callback.openAlarmScreen(ID)
        }
    }

    @Test fun onSetup_bindStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.BIND
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns ID
        every { bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR) } returns COLOR
        every { bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE) } returns TYPE

        viewModel.onSetup(bundle)

        verifySequence {
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            bundle.getString(OpenFrom.INTENT_KEY)
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
            bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE)
            callback.openNoteScreen(ID, COLOR, TYPE)
        }
    }

    @Test fun onSetup_notificationsStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.NOTIFICATIONS

        viewModel.onSetup(bundle)

        verifySequence {
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            bundle.getString(OpenFrom.INTENT_KEY)
            callback.openNotificationScreen()
        }
    }

    @Test fun onSetup_helpDisappearStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.HELP_DISAPPEAR

        viewModel.onSetup(bundle)

        verifySequence {
            callback.sendTidyUpAlarmBroadcast()
            callback.sendNotifyNotesBroadcast()
            callback.sendNotifyInfoBroadcast()

            bundle.getString(OpenFrom.INTENT_KEY)
            callback.openHelpDisappearScreen()
        }
    }


    companion object {
        private const val ID = 33L
        private const val COLOR = 5
        private const val TYPE = 0
    }
}