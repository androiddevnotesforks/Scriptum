package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.preference.IDevelopInteractor
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IDevelopFragment
import kotlin.random.Random

/**
 * Test for [DevelopViewModel].
 */
@ExperimentalCoroutinesApi
class DevelopViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IDevelopFragment

    @MockK lateinit var interactor: IDevelopInteractor

    private val viewModel by lazy { DevelopViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        viewModel.onSetup()

        verifySequence {
            callback.setupPrints()
            callback.setupScreens()
            callback.setupOther()
        }
    }

    @Test fun onClickPrint() {
        val type = PrintType.values().random()

        viewModel.onClickPrint(type)

        verifySequence {
            callback.openPrintScreen(type)
        }
    }

    @Test fun onClickAlarm() = startCoTest {
        val id = Random.nextLong()

        coEvery { interactor.getRandomNoteId() } returns id

        viewModel.onClickAlarm()

        coVerifySequence {
            interactor.getRandomNoteId()
            callback.openAlarmScreen(id)
        }
    }

    @Test fun onClickReset() {
        viewModel.onClickReset()

        verifySequence {
            interactor.resetPreferences()
            callback.showToast(R.string.pref_toast_develop_clear)
        }
    }
}