package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import io.mockk.coVerifySequence
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
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.INotePreferenceFragment
import kotlin.random.Random

/**
 * ViewModel for [NotePreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class NotePreferenceViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var interactor: INotePreferenceInteractor
    @MockK lateinit var callback: INotePreferenceFragment

    private val viewModel by lazy { NotePreferenceViewModel(application) }

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
        val sortSummary = nextString()
        val defaultColorSummary = nextString()
        val savePeriodSummary = nextString()

        every { interactor.getSortSummary() } returns sortSummary
        every { interactor.getDefaultColorSummary() } returns defaultColorSummary
        every { interactor.getSavePeriodSummary() } returns savePeriodSummary

        viewModel.onSetup()

        coVerifySequence {
            callback.setup()

            interactor.getSortSummary()
            callback.updateSortSummary(sortSummary)
            interactor.getDefaultColorSummary()
            callback.updateColorSummary(defaultColorSummary)
            interactor.getSavePeriodSummary()
            callback.updateSavePeriodSummary(savePeriodSummary)
        }
    }

    @Test fun onClickSort() {
        val value = Random.nextInt()

        every { interactor.sort } returns value

        viewModel.onClickSort()

        verifySequence {
            interactor.sort
            callback.showSortDialog(value)
        }
    }

    @Test fun onResultNoteSort() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateSort(value) } returns summary

        viewModel.onResultNoteSort(value)

        verifySequence {
            interactor.updateSort(value)
            callback.updateSortSummary(summary)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onClickNoteColor() {
        val color = Random.nextInt()

        every { interactor.defaultColor } returns color

        viewModel.onClickNoteColor()

        verifySequence {
            interactor.defaultColor
            callback.showColorDialog(color)
        }
    }

    @Test fun onResultNoteColor() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateDefaultColor(value) } returns summary

        viewModel.onResultNoteColor(value)

        verifySequence {
            interactor.updateDefaultColor(value)
            callback.updateColorSummary(summary)
        }
    }

    @Test fun onClickSaveTime() {
        val value = Random.nextInt()

        every { interactor.savePeriod } returns value

        viewModel.onClickSaveTime()

        verifySequence {
            interactor.savePeriod
            callback.showSaveTimeDialog(value)
        }
    }

    @Test fun onResultSaveTime() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateSavePeriod(value) } returns summary

        viewModel.onResultSaveTime(value)

        verifySequence {
            interactor.updateSavePeriod(value)
            callback.updateSavePeriodSummary(summary)
        }
    }

}