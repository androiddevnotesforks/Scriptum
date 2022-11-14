package sgtmelon.scriptum.infrastructure.screen.preference.note

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.screen.preference.ParentPreferenceViewModelTest
import sgtmelon.test.common.nextString

/**
 * ViewModel for [NotePreferenceViewModelImpl].
 */
class NotePreferenceViewModelImplTest : ParentPreferenceViewModelTest() {

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

    override fun verifySetup() {
        getSortSummary()
        getDefaultColorSummary()
        getSavePeriodSummary()
    }

    @Test fun getSort() = getPreferenceTest(
        mockk(),
        { preferencesRepo.sort },
        { viewModel.sort }
    )

    @Test fun `getSortSummary value`() = getSummaryTest(sortSummary) { viewModel.sortSummary }

    @Test fun updateSort() = updateValueTest(
        { getSortSummary(it) },
        { viewModel.updateSort(it) },
        { viewModel.sortSummary }
    )

    @Test fun `getDefaultColorSummary value`() = getSummaryTest(defaultColorSummary) {
        viewModel.defaultColorSummary
    }

    @Test fun getDefaultColor() = getPreferenceTest(
        mockk(),
        { preferencesRepo.defaultColor },
        { viewModel.defaultColor }
    )

    @Test fun updateDefaultColor() = updateValueTest(
        { getDefaultColorSummary(it) },
        { viewModel.updateDefaultColor(it) },
        { viewModel.defaultColorSummary }
    )

    @Test fun getSavePeriod() = getPreferenceTest(
        mockk(),
        { preferencesRepo.savePeriod },
        { viewModel.savePeriod }
    )

    @Test fun `getSavePeriodSummary value`() = getSummaryTest(savePeriodSummary) {
        viewModel.savePeriodSummary
    }

    @Test fun updateSavePeriod() = updateValueTest(
        { getSavePeriodSummary(it) },
        { viewModel.updateSavePeriod(it) },
        { viewModel.savePeriodSummary }
    )
}