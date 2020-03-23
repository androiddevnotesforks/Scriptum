package sgtmelon.scriptum.presentation.screen.vm

import android.os.Bundle
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.model.annotation.OpenFrom
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.SplashViewModel

/**
 * Test for [SplashViewModel].
 */
@ExperimentalCoroutinesApi
class SplashViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: ISplashActivity

    @MockK lateinit var interactor: ISplashInteractor

    private val bundle = mockkClass(Bundle::class)

    private val viewModel by lazy { SplashViewModel(application) }

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
            callback.startIntroActivity()
            callback.startIntroActivity()
        }
    }

    @Test fun onSetup_mainStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns null
        every { interactor.firstStart } returns false

        viewModel.onSetup(bundle = null)
        viewModel.onSetup(bundle)

        verifySequence {
            callback.startMainActivity()
            callback.startMainActivity()
        }
    }

    @Test fun onSetup_alarmStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.ALARM

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns ID

        viewModel.onSetup(bundle)
        verifySequence { callback.startAlarmActivity(ID) }
    }

    @Test fun onSetup_bindStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.BIND

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns ID
        every { bundle.getInt(NoteData.Intent.COLOR, NoteData.Default.COLOR) } returns COLOR
        every { bundle.getInt(NoteData.Intent.TYPE, NoteData.Default.TYPE) } returns TYPE

        viewModel.onSetup(bundle)
        verifySequence { callback.startNoteActivity(ID, COLOR, TYPE) }
    }

    @Test fun onSetup_notificationStart() {
        every { bundle.getString(OpenFrom.INTENT_KEY) } returns OpenFrom.INFO

        viewModel.onSetup(bundle)
        verifySequence { callback.startNotificationActivity() }
    }


    private companion object {
        const val ID = 33L
        const val COLOR = 5
        const val TYPE = 0
    }

}