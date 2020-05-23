package sgtmelon.scriptum.presentation.screen.vm.impl

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.presentation.screen.ui.callback.IPreferenceFragment
import kotlin.random.Random

/**
 * Test fo [PreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class PreferenceViewModelTest : ParentViewModelTest() {


    @MockK lateinit var interactor: IPreferenceInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor
    @MockK lateinit var callback: IPreferenceFragment

    private val viewModel by lazy { PreferenceViewModel(interactor, signalInteractor, callback) }

    override fun setUp() {
        super.setUp()

        every { signalInteractor.melodyList } returns emptyList()
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }



    @Test fun onSetup() {
        val signalState = SignalState(Random.nextBoolean(), Random.nextBoolean())

        val itemList = listOf(
                MelodyItem(Random.nextString(), Random.nextString()),
                MelodyItem(Random.nextString(), Random.nextString()),
                MelodyItem(Random.nextString(), Random.nextString())
        )
        val melodyCheck = itemList.indices.random()

        val themeSummary = Random.nextString()
        val sortSummary = Random.nextString()
        val defaultColorSummary = Random.nextString()
        val savePeriodSummary = Random.nextString()
        val repeatSummary = Random.nextString()
        val typeCheck = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalSummary = Random.nextString()
        val volumeSummary = Random.nextString()

        every { signalInteractor.melodyList } returns itemList

        every { interactor.getThemeSummary() } returns themeSummary
        every { interactor.getSortSummary() } returns sortSummary
        every { interactor.getDefaultColorSummary() } returns defaultColorSummary
        every { interactor.getSavePeriodSummary() } returns savePeriodSummary
        every { interactor.getRepeatSummary() } returns repeatSummary
        every { signalInteractor.typeCheck } returns typeCheck
        every { interactor.getSignalSummary(typeCheck) } returns signalSummary
        every { interactor.getVolumeSummary() } returns volumeSummary

        every { signalInteractor.state } returns null
        viewModel.onSetup()

        every { signalInteractor.state } returns signalState
        every { signalInteractor.melodyCheck } returns -1
        viewModel.onSetup()

        every { signalInteractor.state } returns signalState
        every { signalInteractor.melodyCheck } returns melodyCheck
        viewModel.onSetup()

        verifySequence {
            signalInteractor.melodyList

            signalInteractor.state

            signalInteractor.state
            signalInteractor.melodyCheck

            signalInteractor.state
            signalInteractor.melodyCheck
            callback.apply {
                setupApp()
                setupNote()
                setupNotification(itemList.map { it.title }.toTypedArray())
                setupSave()
                setupOther()

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

                updateMelodyGroupEnabled(signalState.isMelody)
                updateMelodySummary(itemList[melodyCheck].title)
                interactor.getVolumeSummary()
                updateVolumeSummary(volumeSummary)
            }
        }
    }

    @Test fun onClickTheme() {
        val value = Random.nextInt()

        every { interactor.theme } returns value

        assertTrue(viewModel.onClickTheme())

        verifySequence {
            signalInteractor.melodyList

            interactor.theme
            callback.showThemeDialog(value)
        }
    }

    @Test fun onResultTheme() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateTheme(value) } returns summary

        viewModel.onResultTheme(value)

        verifySequence {
            signalInteractor.melodyList

            interactor.updateTheme(value)
            callback.updateThemeSummary(summary)
        }
    }

    @Test fun onClickSort() {
        val value = Random.nextInt()

        every { interactor.sort } returns value

        assertTrue(viewModel.onClickSort())

        verifySequence {
            signalInteractor.melodyList

            interactor.sort
            callback.showSortDialog(value)
        }
    }

    @Test fun onResultNoteSort() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateSort(value) } returns summary

        viewModel.onResultNoteSort(value)

        verifySequence {
            signalInteractor.melodyList

            interactor.updateSort(value)
            callback.updateSortSummary(summary)
        }
    }

    @Test fun onClickNoteColor() {
        val valueColor = Random.nextInt()
        val valueTheme = Random.nextInt()

        every { interactor.defaultColor } returns valueColor
        every { interactor.theme } returns valueTheme

        assertTrue(viewModel.onClickNoteColor())

        verifySequence {
            signalInteractor.melodyList

            interactor.defaultColor
            interactor.theme
            callback.showColorDialog(valueColor, valueTheme)
        }
    }

    @Test fun onResultNoteColor() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateDefaultColor(value) } returns summary

        viewModel.onResultNoteColor(value)

        verifySequence {
            signalInteractor.melodyList

            interactor.updateDefaultColor(value)
            callback.updateColorSummary(summary)
        }
    }

    @Test fun onClickSaveTime() {
        val value = Random.nextInt()

        every { interactor.savePeriod } returns value

        assertTrue(viewModel.onClickSaveTime())

        verifySequence {
            signalInteractor.melodyList

            interactor.savePeriod
            callback.showSaveTimeDialog(value)
        }
    }

    @Test fun onResultSaveTime() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateSavePeriod(value) } returns summary

        viewModel.onResultSaveTime(value)

        verifySequence {
            signalInteractor.melodyList

            interactor.updateSavePeriod(value)
            callback.updateSavePeriodSummary(summary)
        }
    }

    @Test fun onClickRepeat() {
        val value = Random.nextInt()

        every { interactor.repeat } returns value

        assertTrue(viewModel.onClickRepeat())

        verifySequence {
            signalInteractor.melodyList

            interactor.repeat
            callback.showRepeatDialog(value)
        }
    }

    @Test fun onResultRepeat() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateRepeat(value) } returns summary

        viewModel.onResultRepeat(value)

        verifySequence {
            signalInteractor.melodyList

            interactor.updateRepeat(value)
            callback.updateRepeatSummary(summary)
        }
    }

    @Test fun onClickSignal() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }

        every { signalInteractor.typeCheck } returns valueArray

        assertTrue(viewModel.onClickSignal())

        verifySequence {
            signalInteractor.melodyList

            signalInteractor.typeCheck
            callback.showSignalDialog(valueArray)
        }
    }

    @Test fun onResultSignal() {
        val valueArray = BooleanArray(size = 3) { Random.nextBoolean() }
        val signalState = SignalState(Random.nextBoolean(), Random.nextBoolean())
        val summary = Random.nextString()

        every { interactor.updateSignal(valueArray) } returns summary

        every { signalInteractor.state } returns null
        viewModel.onResultSignal(valueArray)

        every { signalInteractor.state } returns signalState
        viewModel.onResultSignal(valueArray)

        verifySequence {
            signalInteractor.melodyList

            signalInteractor.state

            signalInteractor.state
            interactor.updateSignal(valueArray)
            callback.updateSignalSummary(summary)
            callback.updateMelodyGroupEnabled(signalState.isMelody)
        }
    }

    @Test fun onClickMelody() {
        val value = Random.nextInt()

        every { signalInteractor.melodyCheck } returns value

        assertTrue(viewModel.onClickMelody(PermissionResult.LOW_API))
        assertTrue(viewModel.onClickMelody(PermissionResult.ALLOWED))
        assertTrue(viewModel.onClickMelody(PermissionResult.FORBIDDEN))
        assertTrue(viewModel.onClickMelody(PermissionResult.GRANTED))

        verifySequence {
            signalInteractor.melodyList

            signalInteractor.melodyCheck
            callback.showMelodyDialog(value)

            callback.showMelodyPermissionDialog()

            signalInteractor.melodyCheck
            callback.showMelodyDialog(value)

            signalInteractor.melodyCheck
            callback.showMelodyDialog(value)
        }
    }

    @Test fun onSelectMelody_notCorrectValue() {
        val value = Random.nextInt()

        viewModel.onSelectMelody(value)

        verifySequence {
            signalInteractor.melodyList
        }
    }

    @Test fun onSelectMelody() {
        val item = MelodyItem(Random.nextString(), Random.nextString())

        every { signalInteractor.melodyList } returns listOf(item)

        viewModel.onSelectMelody(value = 0)

        verifySequence {
            signalInteractor.melodyList

            callback.playMelody(item.uri)
        }
    }

    @Test fun onResultMelody_notCorrectValue() {
        val value = Random.nextInt()

        viewModel.onResultMelody(value)

        verifySequence {
            signalInteractor.melodyList
        }
    }

    @Test fun onResultMelody() {
        val item = MelodyItem(Random.nextString(), Random.nextString())

        every { signalInteractor.melodyList } returns listOf(item)

        viewModel.onResultMelody(value = 0)

        verifySequence {
            signalInteractor.melodyList

            signalInteractor.setMelodyUri(item.uri)
            callback.updateMelodySummary(item.title)
        }
    }

    @Test fun onClickVolume() {
        val value = Random.nextInt()

        every { interactor.volume } returns value

        assertTrue(viewModel.onClickVolume())

        verifySequence {
            signalInteractor.melodyList

            interactor.volume
            callback.showVolumeDialog(value)
        }
    }

    @Test fun onResultVolume() {
        val value = Random.nextInt()
        val summary = Random.nextString()

        every { interactor.updateVolume(value) } returns summary

        viewModel.onResultVolume(value)

        verifySequence {
            signalInteractor.melodyList

            interactor.updateVolume(value)
            callback.updateVolumeSummary(summary)
        }
    }

}