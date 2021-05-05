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
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel.Companion.IS_LAST_PAGE
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel.Companion.ND_LAST_PAGE
import kotlin.random.Random

/**
 * Test for [IntroViewModel].
 */
@ExperimentalCoroutinesApi
class IntroViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IIntroActivity

    @MockK lateinit var interactor: IIntroInteractor

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { IntroViewModel(application) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, bundle)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        val isLastPage = Random.nextBoolean()

        viewModel.onSetup(bundle = null)

        every { bundle.getBoolean(IS_LAST_PAGE, ND_LAST_PAGE) } returns isLastPage
        viewModel.onSetup(bundle)

        verifySequence {
            callback.setupViewPager(ND_LAST_PAGE)
            callback.setupInsets()

            bundle.getBoolean(IS_LAST_PAGE, ND_LAST_PAGE)
            callback.setupViewPager(isLastPage)
            callback.setupInsets()
        }
    }

    @Test fun onSaveData() {
        val currentPosition = Random.nextInt()
        val itemCount = Random.nextInt()
        val isLastPage = currentPosition == itemCount - 1

        every { callback.getCurrentPosition() } returns currentPosition
        every { callback.getItemCount() } returns itemCount
        every { bundle.putBoolean(IS_LAST_PAGE, isLastPage) } returns Unit
        viewModel.onSaveData(bundle)

        verifySequence {
            callback.getCurrentPosition()
            callback.getItemCount()
            bundle.putBoolean(IS_LAST_PAGE, isLastPage)
        }
    }

    @Test fun onClickEnd() {
        viewModel.onClickEnd()
        verifySequence {
            interactor.onIntroFinish()
            callback.openMainScreen()
        }
    }

}