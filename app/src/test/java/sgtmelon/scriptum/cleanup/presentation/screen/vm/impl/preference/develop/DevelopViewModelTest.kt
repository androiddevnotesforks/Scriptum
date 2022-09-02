package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor

/**
 * Test for [DevelopViewModel].
 */
@ExperimentalCoroutinesApi
class DevelopViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IDevelopFragment
    @MockK lateinit var interactor: DevelopInteractor

    private val viewModel by lazy { DevelopViewModel(callback, interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        viewModel.onSetup()

        verifySequence {
            callback.setupPrints()
            callback.setupScreens()
            callback.setupService()
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