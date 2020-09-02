package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import android.os.Bundle
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.key.ColorShade
import sgtmelon.scriptum.domain.model.state.SignalState
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmActivity
import kotlin.random.Random

/**
 * Test for [AlarmViewModel].
 */
@ExperimentalCoroutinesApi
class AlarmViewModelTest : ParentViewModelTest() {

    private val data = TestData.Note

    @MockK lateinit var callback: IAlarmActivity

    @MockK lateinit var interactor: IAlarmInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { AlarmViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, signalInteractor, bindInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.signalState = firstSignal
        viewModel.onDestroy()

        assertNull(viewModel.callback)
        viewModel.setCallback(callback)
        assertNotNull(viewModel.callback)

        viewModel.signalState = secondSignal
        viewModel.onDestroy()

        assertNull(viewModel.callback)

        verifySequence {
            verifyOnDestroy(firstSignal)
            verifyOnDestroy(secondSignal)
        }
    }

    private fun verifyOnDestroy(state: SignalState) {
        if (state.isMelody) {
            callback.melodyStop()
        }

        if (state.isVibration) {
            callback.vibrateCancel()
        }

        callback.releasePhone()
        interactor.onDestroy()
    }


    @Test fun onSetup_onFirstStart_withGoodModel() = startCoTest {
        val id = Random.nextLong()
        val noteItem = data.thirdNote

        val theme = Random.nextInt()
        val volume = Random.nextInt()
        val volumeIncrease = Random.nextBoolean()

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns id
        coEvery { signalInteractor.getMelodyUri() } returns URI

        every { interactor.theme } returns theme
        every { interactor.volume } returns volume
        every { interactor.volumeIncrease } returns volumeIncrease
        every { signalInteractor.state } returns null
        viewModel.onSetup(bundle)

        every { signalInteractor.state } returns firstSignal
        coEvery { interactor.getModel(id) } returns noteItem
        viewModel.onSetup(bundle)

        coVerifySequence {
            bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                interactor.theme
                setupView(theme)
                setupInsets()

                signalInteractor.getMelodyUri()
                interactor.volume
                interactor.volumeIncrease
                setupPlayer(URI, volume, volumeIncrease)
            }
            signalInteractor.state

            bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                interactor.theme
                setupView(theme)
                setupInsets()

                signalInteractor.getMelodyUri()
                interactor.volume
                interactor.volumeIncrease
                setupPlayer(URI, volume, volumeIncrease)
            }
            signalInteractor.state
            interactor.getModel(id)
            bindInteractor.notifyInfoBind(callback)
            callback.apply {
                prepareLogoAnimation()
                notifyList(noteItem)
                waitLayoutConfigure()
            }
        }
    }

    @Test fun onSetup_onFirstStart_withBadModel() = startCoTest {
        val id = Random.nextLong()

        val theme = Random.nextInt()
        val volume = Random.nextInt()
        val volumeIncrease = Random.nextBoolean()

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns id
        coEvery { signalInteractor.getMelodyUri() } returns URI

        fun callOnSetup() {
            viewModel.id = NoteData.Default.ID
            viewModel.onSetup()
            viewModel.id = NoteData.Default.ID
            viewModel.onSetup(bundle)
        }

        every { interactor.theme } returns theme
        every { interactor.volume } returns volume
        every { interactor.volumeIncrease } returns volumeIncrease
        every { signalInteractor.state } returns null
        callOnSetup()

        every { signalInteractor.state } returns firstSignal
        coEvery { interactor.getModel(any()) } returns null
        callOnSetup()

        coVerifySequence {
            repeat(times = 2) {
                if (it % 2 != 0) bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

                callback.apply {
                    acquirePhone(AlarmViewModel.CANCEL_DELAY)
                    interactor.theme
                    setupView(theme)
                    setupInsets()

                    signalInteractor.getMelodyUri()
                    interactor.volume
                    interactor.volumeIncrease
                    setupPlayer(URI, volume, volumeIncrease)
                }
                signalInteractor.state
            }

            repeat(times = 2) {
                if (it % 2 != 0) bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

                callback.apply {
                    acquirePhone(AlarmViewModel.CANCEL_DELAY)
                    interactor.theme
                    setupView(theme)
                    setupInsets()

                    signalInteractor.getMelodyUri()
                    interactor.volume
                    interactor.volumeIncrease
                    setupPlayer(URI, volume, volumeIncrease)
                }
                signalInteractor.state
                if (it % 2 == 0) {
                    interactor.getModel(NoteData.Default.ID)
                } else {
                    interactor.getModel(id)
                }
                callback.finish()
            }
        }
    }

    @Test fun onSetup_onSecondStart() {
        val id = Random.nextLong()
        val noteItem = data.firstNote.deepCopy()

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns id
        coEvery { signalInteractor.getMelodyUri() } returns null

        viewModel.noteItem = noteItem

        every { interactor.theme } returns Theme.DARK
        every { interactor.volume } returns 1
        every { interactor.volumeIncrease } returns true

        viewModel.onSetup(bundle)
        viewModel.onSetup()

        coVerifySequence {
            bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                interactor.theme
                setupView(Theme.DARK)
                setupInsets()

                signalInteractor.getMelodyUri()
                prepareLogoAnimation()
                notifyList(noteItem)
                waitLayoutConfigure()
            }

            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                interactor.theme
                setupView(Theme.DARK)
                setupInsets()

                signalInteractor.getMelodyUri()
                prepareLogoAnimation()
                notifyList(noteItem)
                waitLayoutConfigure()
            }
        }
    }

    @Test fun onSaveData() {
        val id = Random.nextLong()

        every { bundle.putLong(NoteData.Intent.ID, any()) } returns Unit

        viewModel.id = id
        viewModel.onSaveData(bundle)

        verifySequence { bundle.putLong(NoteData.Intent.ID, id) }
    }

    @Test fun onStart() {
        val firstTheme = Theme.LIGHT
        val secondTheme = Theme.DARK

        val firstNote = data.firstNote.deepCopy()
        val secondNote = data.secondNote.deepCopy()

        every { interactor.theme } returns firstTheme
        viewModel.noteItem = firstNote
        viewModel.signalState = firstSignal
        viewModel.onStart()

        every { interactor.theme } returns secondTheme
        viewModel.noteItem = secondNote
        viewModel.signalState = secondSignal
        viewModel.onStart()

        verifySequence {
            verifyOnStart(firstTheme, ColorShade.ACCENT, firstNote, firstSignal)
            verifyOnStart(secondTheme, ColorShade.DARK, secondNote, secondSignal)
        }
    }

    private fun MockKVerificationScope.verifyOnStart(@Theme theme: Int, colorShade: ColorShade,
                                                     noteItem: NoteItem, state: SignalState) {
        interactor.theme

        callback.apply {
            startRippleAnimation(theme, noteItem.color, colorShade)
            startButtonFadeInAnimation()

            if (state.isMelody) {
                melodyStart()
            }

            if (state.isVibration) {
                startVibratorHandler(AlarmViewModel.START_DELAY, any())
            }

            startLongWaitHandler(AlarmViewModel.CANCEL_DELAY, any())
        }
    }

    @Test fun onClickNote() {
        val noteItem = data.firstNote.deepCopy()

        viewModel.noteItem = noteItem
        viewModel.onClickNote()

        verifySequence {
            callback.openNoteScreen(noteItem)
            callback.finish()
        }
    }

    @Test fun onClickDisable() {
        viewModel.onClickDisable()
        verifySequence { callback.finish() }
    }

    @Test fun onClickRepeat() = startCoTest {
        val noteItem = data.firstNote.deepCopy()
        val repeat = Repeat.MIN_10

        every { callback.getIntArray(R.array.pref_alarm_repeat_array) } returns repeatArray

        viewModel.id = noteItem.id
        viewModel.noteItem = noteItem

        every { interactor.repeat } returns repeat
        viewModel.onClickRepeat()

        coVerifySequence {
            interactor.repeat
            verifyRepeatFinish(repeat, noteItem)
        }
    }

    @Test fun onResultRepeatDialog() = startCoTest {
        val noteItem = data.firstNote.deepCopy()
        val repeat = Repeat.MIN_10

        every { callback.getIntArray(R.array.pref_alarm_repeat_array) } returns repeatArray

        viewModel.id = noteItem.id
        viewModel.noteItem = noteItem

        viewModel.onResultRepeatDialog(R.id.item_repeat_0)
        viewModel.onResultRepeatDialog(R.id.item_repeat_1)
        viewModel.onResultRepeatDialog(R.id.item_repeat_2)
        viewModel.onResultRepeatDialog(R.id.item_repeat_3)
        viewModel.onResultRepeatDialog(R.id.item_repeat_4)

        every { interactor.repeat } returns repeat
        viewModel.onResultRepeatDialog(itemId = -1)

        coVerifySequence {
            verifyRepeatFinish(Repeat.MIN_10, noteItem)
            verifyRepeatFinish(Repeat.MIN_30, noteItem)
            verifyRepeatFinish(Repeat.MIN_60, noteItem)
            verifyRepeatFinish(Repeat.MIN_180, noteItem)
            verifyRepeatFinish(Repeat.MIN_1440, noteItem)

            interactor.repeat
            verifyRepeatFinish(repeat, noteItem)
        }
    }

    private suspend fun verifyRepeatFinish(@Repeat repeat: Int, noteItem: NoteItem) {
        callback.getIntArray(R.array.pref_alarm_repeat_array)
        interactor.setupRepeat(noteItem, repeatArray, repeat)

        callback.showRepeatToast(repeat)
        callback.sendUpdateBroadcast(noteItem.id)
        callback.finish()
    }

    @Test fun onReceiveUnbindNote() {
        val noteItem = data.firstNote.deepCopy(isStatus = true)

        viewModel.noteItem = noteItem
        viewModel.onReceiveUnbindNote(noteItem.id)

        viewModel.id = noteItem.id
        viewModel.onReceiveUnbindNote(noteItem.id)

        assertFalse(noteItem.isStatus)
        verifySequence { callback.notifyList(noteItem) }
    }


    @Test fun getRippleShade() {
        assertEquals(ColorShade.ACCENT, viewModel.getRippleShade(Theme.LIGHT))
        assertEquals(ColorShade.DARK, viewModel.getRippleShade(Theme.DARK))
        assertEquals(ColorShade.DARK, viewModel.getRippleShade(Random.nextInt()))
    }


    private val firstSignal = SignalState(isMelody = false, isVibration = true)
    private val secondSignal = SignalState(isMelody = true, isVibration = false)

    private val repeatArray = intArrayOf(Repeat.MIN_180, Repeat.MIN_1440)

    companion object {
        private const val URI = "testUri"
    }

}