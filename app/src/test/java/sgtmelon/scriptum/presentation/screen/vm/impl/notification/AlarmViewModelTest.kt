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

    private val bundle = mockkClass(Bundle::class)

    private val viewModel by lazy { AlarmViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, signalInteractor, bindInteractor)
    }

    @Test override fun onDestroy() {
        TODO()

        assertNotNull(viewModel.callback)

        viewModel.signalState = signalFirst
        viewModel.onDestroy()

        assertNull(viewModel.callback)
        viewModel.setCallback(callback)
        assertNotNull(viewModel.callback)

        viewModel.signalState = signalSecond
        viewModel.onDestroy()

        assertNull(viewModel.callback)

        verifySequence {
            verifyOnDestroy(signalFirst)
            verifyOnDestroy(signalSecond)
        }
    }

    private fun verifyOnDestroy(signalState: SignalState) {
        if (signalState.isMelody) {
            callback.melodyStop()
        }

        if (signalState.isVibration) {
            callback.vibrateCancel()
        }

        callback.releasePhone()
        interactor.onDestroy()
    }


    @Test fun onSetup_onFirstStart_withGoodModel() = startCoTest {
        TODO()

        val id = Random.nextLong()
        val noteItem = data.noteThird

        every { interactor.theme } returns Theme.LIGHT

        every { signalInteractor.melodyUri } returns URI
        every { interactor.volume } returns 5
        every { interactor.volumeIncrease } returns false

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns id
        coEvery { interactor.getModel(id) } returns noteItem
        every { signalInteractor.state } returns signalFirst

        viewModel.onSetup(bundle)

        coVerifySequence {
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)

                interactor.theme
                setupView(Theme.LIGHT)

                signalInteractor.melodyUri
                interactor.volume
                interactor.volumeIncrease

                setupPlayer(URI, volume = 5, increase = false)
            }

            interactor.getModel(id)
            signalInteractor.state
            bindInteractor.notifyInfoBind(callback)

            callback.apply {
                notifyList(noteItem)
                waitLayoutConfigure()
            }
        }
    }

    @Test fun onSetup_onFirstStart_withBadModel() = startCoTest {
        TODO()

        val id = Random.nextLong()

        every { interactor.theme } returns Theme.LIGHT

        every { signalInteractor.melodyUri } returns URI
        every { interactor.volume } returns 3
        every { interactor.volumeIncrease } returns false

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns id
        coEvery { interactor.getModel(id) } returns null
        every { signalInteractor.state } returns signalFirst

        viewModel.onSetup(bundle)

        coVerifySequence {
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)

                interactor.theme
                setupView(Theme.LIGHT)

                signalInteractor.melodyUri
                interactor.volume
                interactor.volumeIncrease

                setupPlayer(URI, volume = 3, increase = false)
            }

            interactor.getModel(id)
            callback.finish()
        }
    }

    @Test fun onSetup_onSecondStart() {
        TODO()

        val id = Random.nextLong()
        val noteItem = data.noteFirst

        every { interactor.theme } returns Theme.DARK

        every { signalInteractor.melodyUri } returns URI
        every { interactor.volume } returns 1
        every { interactor.volumeIncrease } returns true

        every { bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID) } returns id

        viewModel.noteItem = noteItem
        viewModel.onSetup(bundle)

        verifySequence {
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)

                interactor.theme
                setupView(Theme.DARK)

                signalInteractor.melodyUri
                interactor.volume
                interactor.volumeIncrease

                setupPlayer(URI, volume = 1, increase = true)
            }

            callback.apply {
                notifyList(noteItem)
                waitLayoutConfigure()
            }
        }
    }

    @Test fun onSaveData() {
        TODO()

        val id = Random.nextLong()

        every { bundle.putLong(NoteData.Intent.ID, any()) } returns Unit

        viewModel.id = id
        viewModel.onSaveData(bundle)

        verifySequence { bundle.putLong(NoteData.Intent.ID, id) }
    }

    @Test fun onStart() {
        TODO()

        val themeFirst = Theme.LIGHT
        val themeSecond = Theme.DARK

        val noteFirst = data.noteFirst
        val noteSecond = data.noteSecond

        every { interactor.theme } returns themeFirst

        viewModel.noteItem = noteFirst
        viewModel.signalState = signalFirst
        viewModel.onStart()

        every { interactor.theme } returns themeSecond

        viewModel.noteItem = noteSecond
        viewModel.signalState = signalSecond
        viewModel.onStart()

        verifySequence {
            verifyOnStart(themeFirst, ColorShade.ACCENT, noteFirst, signalFirst)
            verifyOnStart(themeSecond, ColorShade.DARK, noteSecond, signalSecond)
        }
    }

    private fun MockKVerificationScope.verifyOnStart(@Theme theme: Int, colorShade: ColorShade,
                                                     noteItem: NoteItem, signalState: SignalState) {
        TODO()

        interactor.theme

        callback.apply {
            startRippleAnimation(theme, noteItem.color, colorShade)
            startButtonFadeInAnimation()

            if (signalState.isMelody) {
                melodyStart()
            }

            if (signalState.isVibration) {
                startVibratorHandler(AlarmViewModel.START_DELAY, any())
            }

            startLongWaitHandler(AlarmViewModel.CANCEL_DELAY, any())
        }
    }

    @Test fun onClickNote() {
        TODO()

        val noteItem = data.noteFirst

        viewModel.noteItem = noteItem
        viewModel.onClickNote()

        verifySequence {
            callback.startNoteActivity(noteItem)
            callback.finish()
        }
    }

    @Test fun onClickDisable() {
        TODO()

        viewModel.onClickDisable()
        verifySequence { callback.finish() }
    }

    @Test fun onClickRepeat() = startCoTest {
        TODO()

        val repeat = Repeat.MIN_10
        val noteItem = data.noteFirst
        val repeatArray = intArrayOf(Repeat.MIN_180, Repeat.MIN_1440)

        every { interactor.repeat } returns repeat
        every { callback.getIntArray(R.array.pref_alarm_repeat_array) } returns repeatArray

        viewModel.id = noteItem.id
        viewModel.noteItem = noteItem

        viewModel.onClickRepeat()

        coVerifySequence {
            interactor.repeat
            verifyRepeatFinish(repeat, noteItem)
        }
    }

    @Test fun onResultRepeatDialog() = startCoTest {
        TODO()

        val noteItem = data.noteFirst

        every { interactor.repeat } returns Repeat.MIN_10
        every { callback.getIntArray(R.array.pref_alarm_repeat_array) } returns repeatArray

        viewModel.id = noteItem.id
        viewModel.noteItem = noteItem

        viewModel.onResultRepeatDialog(R.id.item_repeat_0)
        viewModel.onResultRepeatDialog(R.id.item_repeat_1)
        viewModel.onResultRepeatDialog(R.id.item_repeat_2)
        viewModel.onResultRepeatDialog(R.id.item_repeat_3)
        viewModel.onResultRepeatDialog(R.id.item_repeat_4)
        viewModel.onResultRepeatDialog(itemId = -1)

        coVerifySequence {
            verifyRepeatFinish(Repeat.MIN_10, noteItem)
            verifyRepeatFinish(Repeat.MIN_30, noteItem)
            verifyRepeatFinish(Repeat.MIN_60, noteItem)
            verifyRepeatFinish(Repeat.MIN_180, noteItem)
            verifyRepeatFinish(Repeat.MIN_1440, noteItem)

            interactor.repeat
            verifyRepeatFinish(Repeat.MIN_10, noteItem)
        }
    }

    private suspend fun verifyRepeatFinish(@Repeat repeat: Int, noteItem: NoteItem) {
        TODO()

        callback.getIntArray(R.array.pref_alarm_repeat_array)
        interactor.setupRepeat(noteItem, repeatArray, repeat)

        callback.showRepeatToast(repeat)
        callback.sendUpdateBroadcast(noteItem.id)
        callback.finish()
    }

    @Test fun onReceiveUnbindNote() {
        TODO()

        val noteItem = data.noteFirst.deepCopy(isStatus = true)

        viewModel.noteItem = noteItem
        viewModel.onReceiveUnbindNote(noteItem.id)

        viewModel.id = noteItem.id
        viewModel.onReceiveUnbindNote(noteItem.id)

        assertFalse(noteItem.isStatus)
        verifySequence { callback.notifyList(noteItem) }
    }


    private val signalFirst = SignalState(isMelody = false, isVibration = true)
    private val signalSecond = SignalState(isMelody = true, isVibration = false)

    private val repeatArray = intArrayOf(Repeat.MIN_180, Repeat.MIN_1440)

    private companion object {
        const val URI = "testUri"
    }
}