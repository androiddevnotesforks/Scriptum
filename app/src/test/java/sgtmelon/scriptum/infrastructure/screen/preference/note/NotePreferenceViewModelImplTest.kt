package sgtmelon.scriptum.infrastructure.screen.preference.note

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.testing.getOrAwaitValue
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest
import sgtmelon.test.common.nextString

/**
 * ViewModel for [NotePreferenceViewModelImpl].
 */
class NotePreferenceViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getSortSummary: GetSummaryUseCase
    @MockK lateinit var getDefaultColorSummary: GetSummaryUseCase
    @MockK lateinit var getSavePeriodSummary: GetSummaryUseCase

    private val sortSummary = nextString()
    private val defaultColorSummary = nextString()
    private val savePeriodSummary = nextString()

    private val viewModel by lazy {
        NotePreferenceViewModelImpl(
            preferencesRepo, getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )
    }

    @Before override fun setUp() {
        super.setUp()
        every { getSortSummary() } returns sortSummary
        every { getDefaultColorSummary() } returns defaultColorSummary
        every { getSavePeriodSummary() } returns savePeriodSummary
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            preferencesRepo, getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )
    }

    //endregion

    private fun verifySetup() {
        getSortSummary()
        getDefaultColorSummary()
        getSavePeriodSummary()
    }

    @Test fun getSort() {
        val sort = mockk<Sort>()

        every { preferencesRepo.sort } returns sort

        assertEquals(viewModel.sort, sort)

        verifySequence {
            verifySetup()
            preferencesRepo.sort
        }
    }

    @Test fun updateSort() {
        val value = Random.nextInt()
        val newSummary = nextString()

        every { getSortSummary(value) } returns newSummary

        viewModel.updateSort(value)

        assertEquals(viewModel.sortSummary.getOrAwaitValue(), newSummary)

        verifySequence {
            verifySetup()
            getSortSummary(value)
        }
    }

    @Test fun getDefaultColor() {
        val color = mockk<Color>()

        every { preferencesRepo.defaultColor } returns color

        assertEquals(viewModel.defaultColor, color)

        verifySequence {
            verifySetup()
            preferencesRepo.defaultColor
        }
    }

    @Test fun updateDefaultColor() {
        val value = Random.nextInt()
        val newSummary = nextString()

        every { getDefaultColorSummary(value) } returns newSummary

        viewModel.updateDefaultColor(value)

        assertEquals(viewModel.defaultColorSummary.getOrAwaitValue(), newSummary)

        verifySequence {
            verifySetup()
            getDefaultColorSummary(value)
        }
    }

    @Test fun getSavePeriod() {
        val savePeriod = mockk<SavePeriod>()

        every { preferencesRepo.savePeriod } returns savePeriod

        assertEquals(viewModel.savePeriod, savePeriod)

        verifySequence {
            verifySetup()
            preferencesRepo.savePeriod
        }
    }

    @Test fun updateSavePeriod() {
        val value = Random.nextInt()
        val newSummary = nextString()

        every { getSavePeriodSummary(value) } returns newSummary

        viewModel.updateSavePeriod(value)

        assertEquals(viewModel.savePeriodSummary.getOrAwaitValue(), newSummary)

        verifySequence {
            verifySetup()
            getSavePeriodSummary(value)
        }
    }
}