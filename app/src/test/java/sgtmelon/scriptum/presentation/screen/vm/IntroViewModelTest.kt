package sgtmelon.scriptum.presentation.screen.vm

import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel

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
        verifySequence { callback.setupViewPager() }
    }

    @Test fun onClickEnd() {
        viewModel.onClickEnd()
        verifySequence {
            interactor.onIntroFinish()
            callback.startMainActivity()
        }
    }

}