package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.domain.model.state.SignalState
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class AlarmPreferenceViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IAlarmPreferenceFragment
    @MockK lateinit var interactor: IAlarmPreferenceInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor

    private val melodyList = TestData.Melody.melodyList

    private val viewModel by lazy {
        AlarmPreferenceViewModel(callback, interactor, signalInteractor)
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, signalInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        val repeatSummary = nextString()
        val typeCheck = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalSummary = nextString()
        val volumeSummary = nextString()

        every { interactor.getRepeatSummary() } returns repeatSummary
        every { signalInteractor.typeCheck } returns typeCheck
        every { interactor.getSignalSummary(typeCheck) } returns signalSummary
        every { interactor.getVolumeSummary() } returns volumeSummary
        coEvery { spyViewModel.setupBackground() } returns Unit

        spyViewModel.onSetup()

        coVerifyOrder {
            spyViewModel.onSetup()

            spyViewModel.callback
            callback.setup()

            spyViewModel.callback
            interactor.getRepeatSummary()
            callback.updateRepeatSummary(repeatSummary)

            spyViewModel.callback
            signalInteractor.typeCheck
            interactor.getSignalSummary(typeCheck)
            callback.updateSignalSummary(signalSummary)

            spyViewModel.callback
            interactor.getVolumeSummary()
            callback.updateVolumeSummary(volumeSummary)

            spyViewModel.setupBackground()
        }
    }

    @Test fun setupBackground_notMelody() = startCoTest {
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupBackground()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupBackground()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupBackground()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupBackground()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = false)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            signalInteractor.getMelodyCheck()
            callback.stopMelodySummarySearch()
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)

            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = false)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.stopMelodySummarySearch()
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)

            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = false)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.stopMelodySummarySearch()
            callback.updateMelodyEnabled(isEnabled = false)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun setupBackground_isMelody() = startCoTest {
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupBackground()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupBackground()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupBackground()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupBackground()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = true)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            signalInteractor.getMelodyCheck()
            callback.stopMelodySummarySearch()
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)

            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = true)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.stopMelodySummarySearch()
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)

            signalInteractor.state
            callback.updateMelodyGroupEnabled(isEnabled = true)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.startMelodySummarySearch()
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.stopMelodySummarySearch()
            callback.updateMelodyEnabled(isEnabled = true)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onPause() {
        viewModel.onPause()

        verifySequence {
            signalInteractor.resetMelodyList()
        }
    }


    @Test fun onClickRepeat() {
        val value = Random.nextInt()

        every { interactor.repeat } returns value

        viewModel.onClickRepeat()

        verifySequence {
            interactor.repeat
            callback.showRepeatDialog(value)
        }
    }

    @Test fun onResultRepeat() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateRepeat(value) } returns summary

        viewModel.onResultRepeat(value)

        verifySequence {
            interactor.updateRepeat(value)
            callback.updateRepeatSummary(summary)
        }
    }

    @Test fun onClickSignal() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }

        every { signalInteractor.typeCheck } returns valueArray

        viewModel.onClickSignal()

        verifySequence {
            signalInteractor.typeCheck
            callback.showSignalDialog(valueArray)
        }
    }

    @Test fun onResultSignal_isMelody() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val summary = nextString()

        every { interactor.updateSignal(valueArray) } returns summary

        every { signalInteractor.state } returns null
        viewModel.onResultSignal(valueArray)

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyList() } returns emptyList()
        viewModel.onResultSignal(valueArray)

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(isEnabled = true)
            callback.updateMelodyEnabled(isEnabled = false)
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(isEnabled = true)
        }
    }

    @Test fun onResultSignal_notMelody() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val summary = nextString()

        every { interactor.updateSignal(valueArray) } returns summary

        every { signalInteractor.state } returns null
        viewModel.onResultSignal(valueArray)

        every { signalInteractor.state } returns state
        viewModel.onResultSignal(valueArray)

        coVerifySequence {
            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state

            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            signalInteractor.state
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

        coEvery { signalInteractor.getMelodyList() } returns emptyList()
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyCheck() } returns Random.nextInt()

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.prepareMelodyDialog()

        coEvery { signalInteractor.getMelodyCheck() } returns index

        viewModel.prepareMelodyDialog()

        coVerifySequence {
            repeat(times = 3) {
                signalInteractor.getMelodyList()
                signalInteractor.getMelodyCheck()
                callback.updateMelodyGroupEnabled(isEnabled = false)
            }

            signalInteractor.getMelodyList()
            signalInteractor.getMelodyCheck()
            callback.showMelodyDialog(titleArray, index)
        }
    }

    @Test fun onSelectMelody_onNull() {
        val index = -1

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            signalInteractor.getMelodyList()
        }
    }

    @Test fun onSelectMelody() {
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.onSelectMelody(index)

        coVerifySequence {
            signalInteractor.getMelodyList()

            callback.playMelody(item.uri)
        }
    }

    @Test fun onResultMelody() {
        val item = melodyList.random()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns item.title

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onResultMelody_onNotEquals() {
        val item = melodyList.random()
        val newTitle = nextString()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns newTitle

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.updateMelodySummary(newTitle)
            callback.showToast(R.string.pref_toast_melody_replace)
        }
    }

    @Test fun onResultMelody_onNull() {
        val item = melodyList.random()

        coEvery { signalInteractor.getMelodyList() } returns melodyList
        coEvery { signalInteractor.setMelodyUri(item.title) } returns null

        viewModel.onResultMelody(item.title)

        coVerifySequence {
            signalInteractor.setMelodyUri(item.title)

            callback.updateMelodyEnabled(isEnabled = false)
            callback.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)
        }
    }

    @Test fun onClickVolume() {
        val value = Random.nextInt()

        every { interactor.volume } returns value

        viewModel.onClickVolume()

        verifySequence {
            interactor.volume
            callback.showVolumeDialog(value)
        }
    }

    @Test fun onResultVolume() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateVolume(value) } returns summary

        viewModel.onResultVolume(value)

        verifySequence {
            interactor.updateVolume(value)
            callback.updateVolumeSummary(summary)
        }
    }

}