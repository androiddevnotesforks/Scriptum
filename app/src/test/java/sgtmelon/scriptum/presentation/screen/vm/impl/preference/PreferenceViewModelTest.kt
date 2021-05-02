package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPreferenceFragment
import kotlin.random.Random

/**
 * Test for [PreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class PreferenceViewModelTest : ParentViewModelTest() {

    @MockK lateinit var interactor: IPreferenceInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor
    @MockK lateinit var callback: IPreferenceFragment

    private val melodyList = TestData.Melody.melodyList

    private val viewModel by lazy { PreferenceViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, signalInteractor)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, signalInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup_forUser() = startCoTest {
        val themeSummary = nextString()
        val sortSummary = nextString()
        val defaultColorSummary = nextString()
        val savePeriodSummary = nextString()
        val repeatSummary = nextString()
        val typeCheck = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalSummary = nextString()
        val volumeSummary = nextString()

        coEvery { spyViewModel.setupMelody() } returns Unit

        every { interactor.isDeveloper } returns false

        every { interactor.getThemeSummary() } returns themeSummary
        every { interactor.getSortSummary() } returns sortSummary
        every { interactor.getDefaultColorSummary() } returns defaultColorSummary
        every { interactor.getSavePeriodSummary() } returns savePeriodSummary
        every { interactor.getRepeatSummary() } returns repeatSummary
        every { signalInteractor.typeCheck } returns typeCheck
        every { interactor.getSignalSummary(typeCheck) } returns signalSummary
        every { interactor.getVolumeSummary() } returns volumeSummary

        spyViewModel.onSetup()

        coVerifyOrder {
            spyViewModel.onSetup()

            spyViewModel.callback
            callback.apply {
                setupApp()
                setupNote()
                setupNotification()
                setupOther()

                interactor.isDeveloper

                interactor.getThemeSummary()
                updateThemeSummary(themeSummary)

                interactor.getSortSummary()
                updateSortSummary(sortSummary)
                interactor.getDefaultColorSummary()
                updateColorSummary(defaultColorSummary)
                interactor.getSavePeriodSummary()
                updateSavePeriodSummary(savePeriodSummary)

                interactor.getRepeatSummary()
                updateRepeatSummary(repeatSummary)
                signalInteractor.typeCheck
                interactor.getSignalSummary(typeCheck)
                updateSignalSummary(signalSummary)

                updateMelodyGroupEnabled(isEnabled = false)
                interactor.getVolumeSummary()
                updateVolumeSummary(volumeSummary)
            }

            spyViewModel.setupMelody()
        }
    }

    @Test fun onSetup_forDeveloper() = startCoTest {
        val themeSummary = nextString()
        val sortSummary = nextString()
        val defaultColorSummary = nextString()
        val savePeriodSummary = nextString()
        val repeatSummary = nextString()
        val typeCheck = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalSummary = nextString()
        val volumeSummary = nextString()

        coEvery { spyViewModel.setupMelody() } returns Unit

        every { interactor.isDeveloper } returns true

        every { interactor.getThemeSummary() } returns themeSummary
        every { interactor.getSortSummary() } returns sortSummary
        every { interactor.getDefaultColorSummary() } returns defaultColorSummary
        every { interactor.getSavePeriodSummary() } returns savePeriodSummary
        every { interactor.getRepeatSummary() } returns repeatSummary
        every { signalInteractor.typeCheck } returns typeCheck
        every { interactor.getSignalSummary(typeCheck) } returns signalSummary
        every { interactor.getVolumeSummary() } returns volumeSummary

        spyViewModel.onSetup()

        coVerifyOrder {
            spyViewModel.onSetup()

            spyViewModel.callback
            callback.apply {
                setupApp()
                setupNote()
                setupNotification()
                setupOther()

                interactor.isDeveloper
                setupDeveloper()

                interactor.getThemeSummary()
                updateThemeSummary(themeSummary)

                interactor.getSortSummary()
                updateSortSummary(sortSummary)
                interactor.getDefaultColorSummary()
                updateColorSummary(defaultColorSummary)
                interactor.getSavePeriodSummary()
                updateSavePeriodSummary(savePeriodSummary)

                interactor.getRepeatSummary()
                updateRepeatSummary(repeatSummary)
                signalInteractor.typeCheck
                interactor.getSignalSummary(typeCheck)
                updateSignalSummary(signalSummary)

                updateMelodyGroupEnabled(isEnabled = false)
                interactor.getVolumeSummary()
                updateVolumeSummary(volumeSummary)
            }

            spyViewModel.setupMelody()
        }
    }

    @Test fun setupMelody_notMelody() = startCoTest {
        val state = SignalState(isMelody = false, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupMelody()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupMelody()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            signalInteractor.getMelodyCheck()

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(state.isMelody)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun setupMelody_isMelody() = startCoTest {
        val state = SignalState(isMelody = true, isVibration = Random.nextBoolean())
        val item = melodyList.random()
        val index = melodyList.indexOf(item)

        every { signalInteractor.state } returns null

        viewModel.setupMelody()

        every { signalInteractor.state } returns state
        coEvery { signalInteractor.getMelodyCheck() } returns null

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyCheck() } returns index
        coEvery { signalInteractor.getMelodyList() } returns emptyList()

        viewModel.setupMelody()

        coEvery { signalInteractor.getMelodyList() } returns melodyList

        viewModel.setupMelody()

        coVerifySequence {
            signalInteractor.state

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            callback.showToast(R.string.pref_toast_melody_empty)

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.showToast(R.string.pref_toast_melody_empty)

            signalInteractor.state
            signalInteractor.getMelodyCheck()
            signalInteractor.getMelodyList()
            callback.updateMelodyGroupEnabled(state.isMelody)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onPause() {
        viewModel.onPause()

        verifySequence {
            signalInteractor.resetMelodyList()
        }
    }


    @Test fun onClickTheme() {
        val value = Random.nextInt()

        every { interactor.theme } returns value

        viewModel.onClickTheme()

        verifySequence {
            interactor.theme
            callback.showThemeDialog(value)
        }
    }

    @Test fun onResultTheme() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateTheme(value) } returns summary

        viewModel.onResultTheme(value)

        verifySequence {
            interactor.updateTheme(value)
            callback.updateThemeSummary(summary)
        }
    }

    //region Note tests

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

    //endregion

    //region Notification tests

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
            callback.showToast(R.string.pref_toast_melody_empty)

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

            callback.showToast(R.string.pref_toast_melody_empty)
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

    //endregion

    @Test fun onUnlockDeveloper() {
        every { interactor.isDeveloper } returns false
        every { interactor.isDeveloper = true } returns Unit
        viewModel.onUnlockDeveloper()

        every { interactor.isDeveloper } returns true
        viewModel.onUnlockDeveloper()

        verifySequence {
            interactor.isDeveloper
            interactor.isDeveloper = true
            callback.setupDeveloper()
            callback.showToast(R.string.pref_toast_develop_unlock)

            interactor.isDeveloper
            callback.showToast(R.string.pref_toast_develop_already)
        }
    }
}