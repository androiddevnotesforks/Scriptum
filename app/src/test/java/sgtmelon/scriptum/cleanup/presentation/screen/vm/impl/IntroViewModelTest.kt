package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.IntroViewModel.Companion.IS_LAST_PAGE
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.IntroViewModel.Companion.ND_LAST_PAGE
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * Test for [IntroViewModel].
 */
@ExperimentalCoroutinesApi
class IntroViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IIntroActivity
    @MockK lateinit var preferencesRepo: PreferencesRepo

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { IntroViewModel(callback, preferencesRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, preferencesRepo, bundle)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

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
            preferencesRepo.isFirstStart = false
            callback.openMainScreen()
        }
    }

}