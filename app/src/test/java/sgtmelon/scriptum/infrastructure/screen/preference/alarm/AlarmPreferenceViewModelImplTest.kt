package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.screen.preference.ParentPreferenceViewModelTest
import sgtmelon.test.common.getRandomSize
import sgtmelon.test.common.nextString
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceViewModelImpl].
 */
class AlarmPreferenceViewModelImplTest : ParentPreferenceViewModelTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getRepeatSummary: GetSummaryUseCase
    @MockK lateinit var getVolumeSummary: GetSummaryUseCase
    @MockK lateinit var getSignalSummary: GetSignalSummaryUseCase
    @MockK lateinit var getMelodyList: GetMelodyListUseCase

    private val signalSummary = nextString()
    private val repeatSummary = nextString()
    private val volumeSummary = nextString()

    private val viewModel by lazy {
        AlarmPreferenceViewModelImpl(
            preferencesRepo, getSignalSummary, getRepeatSummary, getVolumeSummary, getMelodyList
        )
    }

    @Before override fun setUp() {
        super.setUp()
        every { getSignalSummary() } returns signalSummary
        every { getRepeatSummary() } returns repeatSummary
        every { getVolumeSummary() } returns volumeSummary
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            preferencesRepo, getRepeatSummary, getVolumeSummary, getSignalSummary, getMelodyList
        )
    }

    //endregion

    override fun verifySetup() {
        getSignalSummary()
        getRepeatSummary()
        getVolumeSummary()
    }

    @Test fun getSignalTypeCheck() = getPreferenceTest(
        BooleanArray(getRandomSize()) { Random.nextBoolean() },
        { preferencesRepo.signalTypeCheck },
        { viewModel.signalTypeCheck }
    )

    @Test fun `getSignalSummary value`() = getSummaryTest(signalSummary) { viewModel.signalSummary }

    @Test fun updateSignal() {
        TODO()
    }

    @Test fun getRepeat() = getPreferenceTest(
        mockk(),
        { preferencesRepo.repeat },
        { viewModel.repeat }
    )

    @Test fun `getRepeatSummary value`() = getSummaryTest(repeatSummary) { viewModel.repeatSummary }

    @Test fun updateRepeat() = updateValueTest(
        { getRepeatSummary(it) },
        { viewModel.updateRepeat(it) },
        { viewModel.repeatSummary }
    )

    @Test fun getVolumePercent() = getPreferenceTest(
        Random.nextInt(),
        { preferencesRepo.volumePercent },
        { viewModel.volumePercent }
    )

    @Test fun `getVolumeSummary value`() = getSummaryTest(volumeSummary) { viewModel.volumeSummary }

    @Test fun updateVolume() = updateValueTest(
        { getVolumeSummary(it) },
        { viewModel.updateVolume(it) },
        { viewModel.volumeSummary }
    )

    @Test fun todo() {
        TODO()
    }

    /**
    @Test fun getMelodySummaryState() {
        TODO()
    }

    @Test fun getMelodyGroupEnabled() {
        TODO()
    }

    @Test fun getSelectMelodyData() {
        TODO()
    }

    @Test fun `getMelody from empty list`() {
        TODO()
    }

    @Test fun `getMelody with wrong position`() {
        TODO()
    }

    @Test fun getMelody() {
        TODO()
    }

    @Test fun updateMelody() {
        TODO()
    }
    */
}