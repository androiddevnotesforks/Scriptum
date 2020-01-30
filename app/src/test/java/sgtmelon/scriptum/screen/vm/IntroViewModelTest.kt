package sgtmelon.scriptum.screen.vm

import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.screen.ui.callback.IIntroActivity

/**
 * Test for [IntroViewModel].
 */
@ExperimentalCoroutinesApi
class IntroViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IIntroActivity

    @MockK lateinit var interactor: IIntroInteractor

    private val viewModel by lazy { IntroViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        viewModel.onSetup()
        verify(exactly = 1) { callback.setupViewPager() }
    }

    @Test fun onClickEnd() {
        viewModel.onClickEnd()
        verify(exactly = 1) {
            interactor.onIntroFinish()
            callback.startMainActivity()
        }
    }

}