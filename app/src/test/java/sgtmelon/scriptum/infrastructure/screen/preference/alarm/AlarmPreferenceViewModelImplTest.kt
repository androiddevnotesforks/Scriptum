package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [AlarmPreferenceViewModelImpl].
 */
class AlarmPreferenceViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getRepeatSummary: GetSummaryUseCase
    @MockK lateinit var getVolumeSummary: GetSummaryUseCase
    @MockK lateinit var getSignalSummary: GetSignalSummaryUseCase
    @MockK lateinit var getMelodyList: GetMelodyListUseCase

    private val melodyList = TestData.Melody.melodyList

    private val viewModel by lazy {
        AlarmPreferenceViewModelImpl(
            preferencesRepo, getSignalSummary, getRepeatSummary, getVolumeSummary,
            getMelodyList
        )
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            preferencesRepo, getRepeatSummary, getVolumeSummary, getSignalSummary,
            getMelodyList
        )
    }

    //endregion

    @Test fun getRepeat() {
        TODO()
    }

    @Test fun getRepeatSummary() {
        TODO()
    }

    @Test fun updateRepeat() {
        TODO()
    }


    @Test fun getSignalTypeCheck() {
        TODO()
    }

    @Test fun getSignalSummary() {
        TODO()
    }

    @Test fun updateSignal() {
        TODO()
    }


    @Test fun getVolumePercent() {
        TODO()
    }

    @Test fun getVolumeSummary() {
        TODO()
    }

    @Test fun updateVolume() {
        TODO()
    }


    @Test fun getMelodySummaryState() {
        TODO()
    }

    @Test fun getMelodyGroupEnabled() {
        TODO()
    }

    @Test fun getSelectMelodyData() {
        TODO()
    }

    @Test fun getMelody() {
        TODO()
    }

    @Test fun updateMelody() {
        TODO()
    }
}