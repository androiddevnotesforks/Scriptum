package sgtmelon.scriptum.screen.vm

import android.os.Bundle
import io.mockk.Ordering
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity

/**
 * Test for [IntroViewModel]
 */
class IntroViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IIntroActivity

    @MockK lateinit var interactor: IIntroInteractor

    private val viewModel by lazy { IntroViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        assertEquals(callback, viewModel.callback)

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