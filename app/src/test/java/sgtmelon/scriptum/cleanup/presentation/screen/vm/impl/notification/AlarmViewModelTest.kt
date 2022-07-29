package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification

import android.os.Bundle
import io.mockk.MockKVerificationScope
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.state.SignalState
import sgtmelon.scriptum.isDivideTwoEntirely
import sgtmelon.scriptum.parent.ParentViewModelTest

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
    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var interactor: IAlarmInteractor
    @MockK lateinit var getMelodyList: GetMelodyListUseCase

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy {
        AlarmViewModel(callback, preferencesRepo, interactor, getMelodyList)
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, getMelodyList, bundle)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        val state = listOf(firstSignal, secondSignal).random()
        every { preferencesRepo.signalState } returns state

        viewModel.onDestroy()

        assertNull(viewModel.callback)

        verifySequence {
            preferencesRepo.signalState

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

        val melodyList = mockk<List<MelodyItem>>()
        val uri = nextString()
        val volume = Random.nextInt()
        val isVolumeIncrease = Random.nextBoolean()

        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.getMelodyUri(melodyList) } returns uri
        every { preferencesRepo.volume } returns volume
        every { preferencesRepo.isVolumeIncrease } returns isVolumeIncrease
        coEvery { interactor.getModel(id) } returns noteItem

        viewModel.onSetup(bundle)

        coVerifySequence {
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                setupView()
                setupInsets()

                getMelodyList()
                preferencesRepo.getMelodyUri(melodyList)
                preferencesRepo.volume
                preferencesRepo.isVolumeIncrease
                setupPlayer(uri, volume, isVolumeIncrease)
            }

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

        val melodyList = mockk<List<MelodyItem>>()
        val uri = nextString()
        val volume = Random.nextInt()
        val isVolumeIncrease = Random.nextBoolean()

        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.getMelodyUri(melodyList) } returns uri
        every { preferencesRepo.volume } returns volume
        every { preferencesRepo.isVolumeIncrease } returns isVolumeIncrease
        coEvery { interactor.getModel(any()) } returns null

        viewModel.id = Note.Default.ID
        viewModel.onSetup()
        viewModel.id = Note.Default.ID
        viewModel.onSetup(bundle)

        coVerifySequence {
            repeat(times = 2) {
                if (!it.isDivideTwoEntirely()) bundle.getLong(Note.Intent.ID, Note.Default.ID)

                callback.apply {
                    acquirePhone(AlarmViewModel.CANCEL_DELAY)
                    setupView()
                    setupInsets()

                    getMelodyList()
                    preferencesRepo.getMelodyUri(melodyList)
                    preferencesRepo.volume
                    preferencesRepo.isVolumeIncrease
                    setupPlayer(uri, volume, isVolumeIncrease)
                }

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

        val melodyList = mockk<List<MelodyItem>>()
        val uri = nextString()
        val volume = Random.nextInt()
        val isVolumeIncrease = Random.nextBoolean()

        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        coEvery { getMelodyList() } returns melodyList
        coEvery { preferencesRepo.getMelodyUri(melodyList) } returns uri
        every { preferencesRepo.volume } returns volume
        every { preferencesRepo.isVolumeIncrease } returns isVolumeIncrease

        viewModel.noteItem = noteItem
        viewModel.onSetup(bundle)
        viewModel.onSetup()

        coVerifySequence {
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                setupView()
                setupInsets()

                getMelodyList()
                preferencesRepo.getMelodyUri(melodyList)

                preferencesRepo.volume
                preferencesRepo.isVolumeIncrease
                setupPlayer(uri, volume, isVolumeIncrease)

                prepareLogoAnimation()
                notifyList(noteItem)
            }

            callback.apply {
                acquirePhone(AlarmViewModel.CANCEL_DELAY)
                setupView()
                setupInsets()

                getMelodyList()
                preferencesRepo.getMelodyUri(melodyList)
                preferencesRepo.volume
                preferencesRepo.isVolumeIncrease
                setupPlayer(uri, volume, isVolumeIncrease)

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

    @Test fun `onStart first signal`() {
        val firstNote = data.firstNote.deepCopy()

        every { preferencesRepo.signalState } returns firstSignal

        viewModel.noteItem = firstNote
        viewModel.onStart()

        verifySequence {
            verifyOnStart(firstNote, firstSignal)
        }
    }

    @Test fun `onStart second signal`() {
        val secondNote = data.secondNote.deepCopy()

        every { preferencesRepo.signalState } returns secondSignal

        viewModel.noteItem = secondNote
        viewModel.onStart()

        verifySequence {
            verifyOnStart(secondNote, secondSignal)
        }
    }

    private fun MockKVerificationScope.verifyOnStart(noteItem: NoteItem, state: SignalState) {
        preferencesRepo.signalState

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

    @Test fun onClickRepeat() {
        val repeat = mockk<Repeat>()

        every { preferencesRepo.repeat } returns repeat
        every { spyViewModel.repeatFinish(repeat) } returns Unit

        spyViewModel.onClickRepeat()

        verifySequence {
            spyViewModel.onClickRepeat()
            preferencesRepo.repeat
            spyViewModel.repeatFinish(repeat)
        }
    }

    @Test fun onResultRepeatDialog() {
        val itemId = Random.nextInt()
        val repeatFirst = mockk<Repeat>()
        val repeatSecond = mockk<Repeat>()

        every { spyViewModel.getRepeatById(itemId) } returns null
        every { preferencesRepo.repeat } returns repeatFirst
        every { spyViewModel.repeatFinish(repeatFirst) } returns Unit

        spyViewModel.onResultRepeatDialog(itemId)

        every { spyViewModel.getRepeatById(itemId) } returns repeatSecond
        every { spyViewModel.repeatFinish(repeatSecond) } returns Unit

        spyViewModel.onResultRepeatDialog(itemId)

        verifySequence {
            spyViewModel.onResultRepeatDialog(itemId)
            spyViewModel.getRepeatById(itemId)
            preferencesRepo.repeat
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
        val repeat = mockk<Repeat>()
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
}