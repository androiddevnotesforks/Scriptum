package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification

import android.os.Bundle
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.state.SignalState
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.isDivideTwoEntirely
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.IAlarmActivity
import java.util.*
import kotlin.random.Random

/**
 * Test for [AlarmViewModel].
 */
@ExperimentalCoroutinesApi
class AlarmViewModelTest : ParentViewModelTest() {

    //region Data

    private val firstSignal = SignalState(isMelody = false, isVibration = true)
    private val secondSignal = SignalState(isMelody = true, isVibration = false)

    //endregion

    //region Setup

    private val data = TestData.Note

    @MockK lateinit var callback: IAlarmActivity
    @MockK lateinit var interactor: IAlarmInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { AlarmViewModel(callback, interactor, signalInteractor) }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, signalInteractor, bundle)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        val state = listOf(firstSignal, secondSignal).random()
        viewModel.signalState = state
        viewModel.onDestroy()

        assertNull(viewModel.callback)

        verifySequence {
            if (state.isMelody) {
                callback.melodyStop()
            }

            if (state.isVibration) {
                callback.vibrateCancel()
            }

            callback.releasePhone()
            interactor.onDestroy()
        }
    }

    //endregion


    @Test fun onSetup_onFirstStart_withGoodModel() = startCoTest {
        val id = Random.nextLong()
        val noteItem = data.thirdNote

        val volume = Random.nextInt()
        val isVolumeIncrease = Random.nextBoolean()

        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        coEvery { signalInteractor.getMelodyUri() } returns URI

        every { interactor.volume } returns volume
        every { interactor.isVolumeIncrease } returns isVolumeIncrease
        every { signalInteractor.state } returns null
        viewModel.onSetup(bundle)

        every { signalInteractor.state } returns firstSignal
        coEvery { interactor.getModel(id) } returns noteItem
        viewModel.onSetup(bundle)

        coVerifySequence {
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                setupView()
                setupInsets()

                signalInteractor.getMelodyUri()
                interactor.volume
                interactor.isVolumeIncrease
                setupPlayer(URI, volume, isVolumeIncrease)
            }
            signalInteractor.state

            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                setupView()
                setupInsets()

                signalInteractor.getMelodyUri()
                interactor.volume
                interactor.isVolumeIncrease
                setupPlayer(URI, volume, isVolumeIncrease)
            }
            signalInteractor.state
            interactor.getModel(id)
            callback.sendNotifyInfoBroadcast()
            callback.apply {
                prepareLogoAnimation()
                notifyList(noteItem)
            }
        }
    }

    @Test fun onSetup_onFirstStart_withBadModel() = startCoTest {
        val id = Random.nextLong()

        val volume = Random.nextInt()
        val isVolumeIncrease = Random.nextBoolean()

        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        coEvery { signalInteractor.getMelodyUri() } returns URI

        fun callOnSetup() {
            viewModel.id = Note.Default.ID
            viewModel.onSetup()
            viewModel.id = Note.Default.ID
            viewModel.onSetup(bundle)
        }

        every { interactor.volume } returns volume
        every { interactor.isVolumeIncrease } returns isVolumeIncrease
        every { signalInteractor.state } returns null
        callOnSetup()

        every { signalInteractor.state } returns firstSignal
        coEvery { interactor.getModel(any()) } returns null
        callOnSetup()

        coVerifySequence {
            repeat(times = 2) {
                if (!it.isDivideTwoEntirely()) {
                    bundle.getLong(Note.Intent.ID, Note.Default.ID)
                }

                callback.apply {
                    acquirePhone(AlarmViewModel.CANCEL_DELAY)
                    setupView()
                    setupInsets()

                    signalInteractor.getMelodyUri()
                    interactor.volume
                    interactor.isVolumeIncrease
                    setupPlayer(URI, volume, isVolumeIncrease)
                }
                signalInteractor.state
            }

            repeat(times = 2) {
                if (!it.isDivideTwoEntirely()) bundle.getLong(Note.Intent.ID, Note.Default.ID)

                callback.apply {
                    acquirePhone(AlarmViewModel.CANCEL_DELAY)
                    setupView()
                    setupInsets()

                    signalInteractor.getMelodyUri()
                    interactor.volume
                    interactor.isVolumeIncrease
                    setupPlayer(URI, volume, isVolumeIncrease)
                }
                signalInteractor.state
                if (it.isDivideTwoEntirely()) {
                    interactor.getModel(Note.Default.ID)
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

        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        coEvery { signalInteractor.getMelodyUri() } returns null

        viewModel.noteItem = noteItem

        every { interactor.volume } returns 1
        every { interactor.isVolumeIncrease } returns true

        viewModel.onSetup(bundle)
        viewModel.onSetup()

        coVerifySequence {
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                setupView()
                setupInsets()

                signalInteractor.getMelodyUri()
                prepareLogoAnimation()
                notifyList(noteItem)
            }

            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                setupView()
                setupInsets()

                signalInteractor.getMelodyUri()
                prepareLogoAnimation()
                notifyList(noteItem)
            }
        }
    }

    @Test fun onSaveData() {
        val id = Random.nextLong()

        every { bundle.putLong(Note.Intent.ID, any()) } returns Unit

        viewModel.id = id
        viewModel.onSaveData(bundle)

        verifySequence { bundle.putLong(Note.Intent.ID, id) }
    }

    @Test fun onStart() {
        val firstNote = data.firstNote.deepCopy()
        val secondNote = data.secondNote.deepCopy()

        viewModel.noteItem = firstNote
        viewModel.signalState = firstSignal
        viewModel.onStart()

        viewModel.noteItem = secondNote
        viewModel.signalState = secondSignal
        viewModel.onStart()

        verifySequence {
            verifyOnStart(firstNote, firstSignal)
            verifyOnStart(secondNote, secondSignal)
        }
    }

    private fun MockKVerificationScope.verifyOnStart(noteItem: NoteItem, state: SignalState) {
        callback.apply {
            startRippleAnimation(noteItem.color)
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
        val repeat = Random.nextInt()

        every { interactor.repeat } returns repeat
        every { spyViewModel.repeatFinish(repeat) } returns Unit

        spyViewModel.onClickRepeat()

        verifySequence {
            spyViewModel.onClickRepeat()
            interactor.repeat
            spyViewModel.repeatFinish(repeat)
        }
    }

    @Test fun onResultRepeatDialog() = startCoTest {
        val itemId = Random.nextInt()
        val repeatFirst = Random.nextInt()
        val repeatSecond = Random.nextInt()

        every { spyViewModel.getRepeatById(itemId) } returns null
        every { interactor.repeat } returns repeatFirst
        every { spyViewModel.repeatFinish(repeatFirst) } returns Unit

        spyViewModel.onResultRepeatDialog(itemId)

        every { spyViewModel.getRepeatById(itemId) } returns repeatSecond
        every { spyViewModel.repeatFinish(repeatSecond) } returns Unit

        spyViewModel.onResultRepeatDialog(itemId)

        verifySequence {
            spyViewModel.onResultRepeatDialog(itemId)
            spyViewModel.getRepeatById(itemId)
            interactor.repeat
            spyViewModel.repeatFinish(repeatFirst)

            spyViewModel.onResultRepeatDialog(itemId)
            spyViewModel.getRepeatById(itemId)
            spyViewModel.repeatFinish(repeatSecond)
        }
    }

    @Test fun getRepeatById() {
        assertEquals(Repeat.MIN_10, viewModel.getRepeatById(R.id.item_repeat_0))
        assertEquals(Repeat.MIN_30, viewModel.getRepeatById(R.id.item_repeat_1))
        assertEquals(Repeat.MIN_60, viewModel.getRepeatById(R.id.item_repeat_2))
        assertEquals(Repeat.MIN_180, viewModel.getRepeatById(R.id.item_repeat_3))
        assertEquals(Repeat.MIN_1440, viewModel.getRepeatById(R.id.item_repeat_4))
        assertNull(viewModel.getRepeatById(itemId = -1))
    }

    @Test fun repeatFinish() {
        val id = Random.nextLong()
        val item = mockk<NoteItem>()
        val repeat = Random.nextInt()
        val repeatArray = IntArray(getRandomSize()) { Random.nextInt() }
        val calendar = mockk<Calendar>()

        every { callback.getIntArray(R.array.pref_alarm_repeat_array) } returns repeatArray

        viewModel.id = id
        viewModel.noteItem = item

        coEvery { interactor.setupRepeat(item, repeatArray, repeat) } returns null
        viewModel.repeatFinish(repeat)

        coEvery { interactor.setupRepeat(item, repeatArray, repeat) } returns calendar
        viewModel.repeatFinish(repeat)

        coVerifySequence {
            callback.getIntArray(R.array.pref_alarm_repeat_array)
            interactor.setupRepeat(item, repeatArray, repeat)
            callback.showRepeatToast(repeat)
            callback.sendUpdateBroadcast(id)
            callback.finish()

            callback.getIntArray(R.array.pref_alarm_repeat_array)
            interactor.setupRepeat(item, repeatArray, repeat)
            callback.sendSetAlarmBroadcast(id, calendar, showToast = false)
            callback.sendNotifyInfoBroadcast()
            callback.showRepeatToast(repeat)
            callback.sendUpdateBroadcast(id)
            callback.finish()
        }
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

    companion object {
        private const val URI = "testUri"
    }
}