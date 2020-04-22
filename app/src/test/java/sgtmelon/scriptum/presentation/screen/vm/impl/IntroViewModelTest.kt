package sgtmelon.scriptum.presentation.screen.vm.impl

import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity

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
        TODO()

        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        TODO()

        viewModel.onSetup()
        verifySequence { callback.setupViewPager() }
    }

    @Test fun onClickEnd() {
        TODO()

        viewModel.onClickEnd()
        verifySequence {
            interactor.onIntroFinish()
            callback.startMainActivity()
        }
    }

}