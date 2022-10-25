package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentCoTest
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.state.SignalState
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest
import sgtmelon.test.common.nextString

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
            preferencesRepo, getRepeatSummary, getSignalSummary, getVolumeSummary,
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