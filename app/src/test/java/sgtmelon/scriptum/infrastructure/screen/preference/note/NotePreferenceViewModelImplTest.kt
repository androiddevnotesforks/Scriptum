package sgtmelon.scriptum.infrastructure.screen.preference.note

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.test.common.nextString

/**
 * ViewModel for [NotePreferenceViewModelImpl].
 */
@ExperimentalCoroutinesApi
class NotePreferenceViewModelImplTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getSortSummary: GetSummaryUseCase
    @MockK lateinit var getDefaultColorSummary: GetSummaryUseCase
    @MockK lateinit var getSavePeriodSummary: GetSummaryUseCase

    private val viewModel by lazy {
        NotePreferenceViewModelImpl(
            callback, preferencesRepo,
            getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            callback, preferencesRepo,
            getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )
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

        every { getSortSummary() } returns sortSummary
        every { getDefaultColorSummary() } returns defaultColorSummary
        every { getSavePeriodSummary() } returns savePeriodSummary

        viewModel.onSetup()

        coVerifySequence {
            callback.setup()

            getSortSummary()
            callback.updateSortSummary(sortSummary)
            getDefaultColorSummary()
            callback.updateColorSummary(defaultColorSummary)
            getSavePeriodSummary()
            callback.updateSavePeriodSummary(savePeriodSummary)
        }
    }

    @Test fun onClickSort() {
        val value = mockk<Sort>()

        every { preferencesRepo.sort } returns value

        viewModel.onClickSort()

        verifySequence {
            preferencesRepo.sort
            callback.showSortDialog(value)
        }
    }

    @Test fun onResultNoteSort() {
        val value = Random.nextInt()
        val summary = nextString()

        every { getSortSummary(value) } returns summary

        viewModel.updateSort(value)

        verifySequence {
            getSortSummary(value)
            callback.updateSortSummary(summary)
            callback.sendNotifyNotesBroadcast()
        }
    }

    @Test fun onClickNoteColor() {
        val color = mockk<Color>()

        every { preferencesRepo.defaultColor } returns color

        viewModel.onClickNoteColor()

        verifySequence {
            preferencesRepo.defaultColor
            callback.showColorDialog(color)
        }
    }

    @Test fun onResultNoteColor() {
        val value = Random.nextInt()
        val summary = nextString()

        every { getDefaultColorSummary(value) } returns summary

        viewModel.updateDefaultColor(value)

        verifySequence {
            getDefaultColorSummary(value)
            callback.updateColorSummary(summary)
        }
    }

    @Test fun onClickSaveTime() {
        val value = mockk<SavePeriod>()

        every { preferencesRepo.savePeriod } returns value

        viewModel.onClickSaveTime()

        verifySequence {
            preferencesRepo.savePeriod
            callback.showSaveTimeDialog(value)
        }
    }

    @Test fun onResultSaveTime() {
        val value = Random.nextInt()
        val summary = nextString()

        every { getSavePeriodSummary(value) } returns summary

        viewModel.updateSavePeriod(value)

        verifySequence {
            getSavePeriodSummary(value)
            callback.updateSavePeriodSummary(summary)
        }
    }
}