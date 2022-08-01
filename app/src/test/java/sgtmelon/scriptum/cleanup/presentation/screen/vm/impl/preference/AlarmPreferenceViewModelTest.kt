package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

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
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.state.SignalState

/**
 * Test for [AlarmPreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class AlarmPreferenceViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IAlarmPreferenceFragment
    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getRepeatSummary: GetSummaryUseCase
    @MockK lateinit var getVolumeSummary: GetSummaryUseCase
    @MockK lateinit var getSignalSummary: GetSignalSummaryUseCase
    @MockK lateinit var getMelodyList: GetMelodyListUseCase

    private val melodyList = TestData.Melody.melodyList

    private val viewModel by lazy {
        AlarmPreferenceViewModel(
            callback,
            preferencesRepo, getRepeatSummary, getVolumeSummary, getSignalSummary,
            getMelodyList
        )
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            callback,
            preferencesRepo, getRepeatSummary, getVolumeSummary, getSignalSummary,
            getMelodyList
        )
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        val repeatSummary = nextString()
        val signalSummary = nextString()
        val volumeSummary = nextString()

        every { getRepeatSummary() } returns repeatSummary
        every { getSignalSummary() } returns signalSummary
        every { getVolumeSummary() } returns volumeSummary
        coEvery { spyViewModel.setupBackground() } returns Unit

        spyViewModel.onSetup()

        coVerifyOrder {
            spyViewModel.onSetup()

            spyViewModel.callback
            callback.setup()

            spyViewModel.callback
            getRepeatSummary()
            callback.updateRepeatSummary(repeatSummary)

            spyViewModel.callback
            getSignalSummary()
            callback.updateSignalSummary(signalSummary)

            spyViewModel.callback
            getVolumeSummary()
            callback.updateVolumeSummary(volumeSummary)

            spyViewModel.setupBackground()
        }
    }

    @Test fun setupBackground() {
        val isMelody = Random.nextBoolean()
        val state = SignalState(isMelody, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { preferencesRepo.signalState } returns state
        coEvery { getMelodyList() } returns emptyList()
        coEvery { preferencesRepo.getMelodyCheck(emptyList()) } returns index

        runBlocking { viewModel.setupBackground() }

        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.getMelodyCheck(melodyList) } returns index

        runBlocking { viewModel.setupBackground() }

        coVerifySequence {
            preferencesRepo.signalState
            callback.updateMelodyGroupEnabled(isMelody)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            getMelodyList()
            preferencesRepo.getMelodyCheck(emptyList())
            callback.stopMelodySummarySearch()
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)

            preferencesRepo.signalState
            callback.updateMelodyGroupEnabled(isMelody)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            getMelodyList()
            preferencesRepo.getMelodyCheck(melodyList)
            callback.stopMelodySummarySearch()
            callback.updateMelodyEnabled(isMelody)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onPause() {
        viewModel.onPause()

        verifySequence {
            getMelodyList.reset()
        }
    }


    @Test fun onClickRepeat() {
        val value = mockk<Repeat>()

        every { preferencesRepo.repeat } returns value

        viewModel.onClickRepeat()

        verifySequence {
            preferencesRepo.repeat
            callback.showRepeatDialog(value)
        }
    }

    @Test fun onResultRepeat() {
        val value = Random.nextInt()
        val summary = nextString()

        every { getRepeatSummary(value) } returns summary

        viewModel.onResultRepeat(value)

        verifySequence {
            getRepeatSummary(value)
            callback.updateRepeatSummary(summary)
        }
    }

    @Test fun onClickSignal() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }

        every { preferencesRepo.signalTypeCheck } returns valueArray

        viewModel.onClickSignal()

        verifySequence {
            preferencesRepo.signalTypeCheck
            callback.showSignalDialog(valueArray)
        }
    }

    @Test fun `onResultSignal and it's a melody`() {
        val valueArray = BooleanArray(getRandomSize()) { Random.nextBoolean() }
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val summary = nextString()

        every { getSignalSummary(valueArray) } returns summary
        every { preferencesRepo.signalState } returns state
        coEvery { getMelodyList() } returns emptyList()
        viewModel.onResultSignal(valueArray)

        coEvery { getMelodyList() } returns melodyList
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            getSignalSummary(valueArray)
            callback.updateSignalSummary(summary)
            preferencesRepo.signalState
            getMelodyList()
            callback.updateMelodyGroupEnabled(isEnabled = true)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)

            getSignalSummary(valueArray)
            callback.updateSignalSummary(summary)
            preferencesRepo.signalState
            getMelodyList()
            callback.updateMelodyGroupEnabled(isEnabled = true)
        }
    }

    @Test fun `onResultSignal and it's not a melody`() {
        val valueArray = BooleanArray(getRandomSize()) { Random.nextBoolean() }
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val summary = nextString()

        every { getSignalSummary(valueArray) } returns summary

        every { preferencesRepo.signalState } returns state
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            getSignalSummary(valueArray)
            callback.updateSignalSummary(summary)
            preferencesRepo.signalState
            callback.updateMelodyGroupEnabled(isEnabled = false)
        }
    }

    @Test fun onClickMelody() = startCoTest {
        coEvery { spyViewModel.prepareMelodyDialog() } returns Unit

        for (it in PermissionResult.values()) {
            spyViewModel.onClickMelody(it)
        }

        spyViewModel.onClickMelody(result = null)

        coVerifyOrder {
            spyViewModel.onClickMelody(PermissionResult.LOW_API)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(PermissionResult.ALLOWED)
            spyViewModel.callback
            callback.showMelodyPermissionDialog()

            spyViewModel.onClickMelody(PermissionResult.FORBIDDEN)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(PermissionResult.GRANTED)
            spyViewModel.prepareMelodyDialog()

            spyViewModel.onClickMelody(result = null)
        }
    }

    @Test fun prepareMelodyDialog() = startCoTest {
        val index = Random.nextInt()
        val titleArray = melodyList.map { it.title }.toTypedArray()

        coEvery { getMelodyList() } returns emptyList()
        coEvery { preferencesRepo.getMelodyCheck(emptyList()) } returns null

        viewModel.prepareMelodyDialog()

        coEvery { preferencesRepo.getMelodyCheck(emptyList()) } returns Random.nextInt()

        viewModel.prepareMelodyDialog()

        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.getMelodyCheck(melodyList) } returns null

        viewModel.prepareMelodyDialog()

        coEvery { preferencesRepo.getMelodyCheck(melodyList) } returns index

        viewModel.prepareMelodyDialog()

        coVerifySequence {
            repeat(times = 2) {
                getMelodyList()
                preferencesRepo.getMelodyCheck(emptyList())
                callback.updateMelodyGroupEnabled(isEnabled = false)
            }

            getMelodyList()
            preferencesRepo.getMelodyCheck(melodyList)
            callback.updateMelodyGroupEnabled(isEnabled = false)

            getMelodyList()
            preferencesRepo.getMelodyCheck(melodyList)
            callback.showMelodyDialog(titleArray, index)
        }
    }

    @Test fun onSelectMelody_onNull() {
        val index = -1

        coEvery { getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            getMelodyList()
        }
    }

    @Test fun onSelectMelody() {
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        coEvery { getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            getMelodyList()

            callback.playMelody(item.uri)
        }
    }

    @Test fun onResultMelody() {
        val item = melodyList.random()

        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.setMelodyUri(melodyList, item.title) } returns item.title

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            getMelodyList()
            preferencesRepo.setMelodyUri(melodyList, item.title)

            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onResultMelody_onNotEquals() {
        val item = melodyList.random()
        val newTitle = nextString()

        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.setMelodyUri(melodyList, item.title) } returns newTitle

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            getMelodyList()
            preferencesRepo.setMelodyUri(melodyList, item.title)

            callback.updateMelodySummary(newTitle)
            callback.showToast(R.string.pref_toast_melody_replace)
        }
    }

    @Test fun onResultMelody_onNull() {
        val item = melodyList.random()

        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.setMelodyUri(melodyList, item.title) } returns null

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            getMelodyList()
            preferencesRepo.setMelodyUri(melodyList, item.title)

            callback.updateMelodyEnabled(isEnabled = false)
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)
        }
    }

    @Test fun onClickVolume() {
        val value = Random.nextInt()

        every { preferencesRepo.volume } returns value

        viewModel.onClickVolume()

        verifySequence {
            preferencesRepo.volume
            callback.showVolumeDialog(value)
        }
    }

    @Test fun onResultVolume() {
        val value = Random.nextInt()
        val summary = nextString()

        every { getVolumeSummary(value) } returns summary

        viewModel.onResultVolume(value)

        verifySequence {
            getVolumeSummary(value)
            callback.updateVolumeSummary(summary)
        }
    }
}