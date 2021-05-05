package sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import kotlin.random.Random

/**
 * Test for [DevelopViewModel].
 */
@ExperimentalCoroutinesApi
class DevelopViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IDevelopFragment

    @MockK lateinit var interactor: IDevelopInteractor

    private val viewModel by lazy { DevelopViewModel(application) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

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