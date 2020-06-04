package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.os.Bundle
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getCalendar
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteFragment
import java.util.*
import kotlin.random.Random

/**
 * Test for [RollNoteViewModel].
 */
@ExperimentalCoroutinesApi
class RollNoteViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IRollNoteFragment
    @MockK lateinit var parentCallback: INoteChild

    @MockK lateinit var interactor: IRollNoteInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    @MockK lateinit var inputControl: IInputControl

    private val viewModel by lazy { RollNoteViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor, bindInteractor)
        viewModel.setInputControl(inputControl)

        assertEquals(NoteData.Default.ID, viewModel.id)
        assertEquals(NoteData.Default.COLOR, viewModel.color)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        assertNotNull(viewModel.parentCallback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        assertNull(viewModel.parentCallback)

        verifySequence {
            interactor.onDestroy()

            TODO()
        }
    }


    @Test fun onSetup() {
        TODO()
    }


    @Test fun onSaveData() {
        val id = Random.nextLong()
        val color = Random.nextInt()
        val bundle = mockk<Bundle>()

        every { bundle.putLong(NoteData.Intent.ID, id) } returns Unit
        every { bundle.putInt(NoteData.Intent.COLOR, color) } returns Unit

        viewModel.id = id
        viewModel.color = color
        viewModel.onSaveData(bundle)

        verifySequence {
            bundle.putLong(NoteData.Intent.ID, id)
            bundle.putInt(NoteData.Intent.COLOR, color)
        }
    }

    @Test fun onResume() {
        TODO()
    }

    @Test fun onPause() {
        TODO()
    }


    @Test fun onClickBackArrow() {
        TODO()
    }

    @Test fun onPressBack() {
        TODO()
    }


    @Test fun onClickVisible() {
        TODO()
    }

    @Test fun onUpdateInfo() {
        TODO()
    }

    @Test fun onEditorClick() {
        TODO()
    }

    @Test fun onClickAdd() {
        TODO()
    }

    @Test fun onClickItemCheck() {
        TODO()
    }

    @Test fun onLongClickItemCheck() {
        TODO()
    }


    @Test fun onResultColorDialog() {
        TODO()
    }

    @Test fun onResultRankDialog() {
        TODO()
    }

    @Test fun onResultDateDialog() {
        TODO()
    }

    @Test fun onResultDateDialogClear() {
        TODO()
    }

    @Test fun onResultTimeDialog() {
        TODO()
    }

    @Test fun onResultConvertDialog() {
        TODO()
    }


    @Test fun onReceiveUnbindNote() {
        viewModel.onReceiveUnbindNote(Random.nextLong())

        val id = Random.nextLong()
        val noteItem = mockk<NoteItem.Roll>()
        val restoreItem = mockk<NoteItem.Roll>()

        every { noteItem.isStatus = false } returns Unit
        every { restoreItem.isStatus = false } returns Unit

        viewModel.id = id
        viewModel.noteItem = noteItem
        viewModel.restoreItem = restoreItem

        viewModel.onReceiveUnbindNote(id)

        verifySequence {
            noteItem.isStatus = false
            restoreItem.isStatus = false

            callback.onBindingNote(noteItem)
        }
    }


    @Test fun onMenuRestore() {
        TODO()
    }

    @Test fun onMenuRestoreOpen() {
        TODO()
    }

    @Test fun onMenuClear() {
        TODO()
    }

    @Test fun onMenuUndo() {
        TODO()
    }

    @Test fun onMenuRedo() {
        TODO()
    }

    @Test fun onMenuRank() {
        TODO()
    }

    @Test fun onMenuColor() {
        TODO()
    }

    @Test fun onMenuSave() {
        TODO()
    }

    @Test fun onMenuNotification() {
        val noteItem = mockk<NoteItem.Roll>()
        val alarmDate = Random.nextString()
        val haveAlarm = Random.nextBoolean()
        val calendar = mockk<Calendar>()

        val noteState = mockk<NoteState>()

        every { noteItem.alarmDate } returns alarmDate
        every { noteItem.haveAlarm() } returns haveAlarm

        mockkStatic("sgtmelon.extension.TimeExtensionUtils")
        every { alarmDate.getCalendar() } returns calendar

        viewModel.noteItem = noteItem
        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        viewModel.onMenuNotification()

        every { noteState.isEdit } returns true
        viewModel.onMenuNotification()

        verifySequence {
            noteState.isEdit

            noteItem.alarmDate
            alarmDate.getCalendar()
            noteItem.haveAlarm()
            callback.showDateDialog(calendar, haveAlarm)

            noteState.isEdit
        }
    }

    @Test fun onMenuBind() = startCoTest {
        val noteItem = mockk<NoteItem.Roll>()
        val restoreItem = mockk<NoteItem.Roll>()

        val noteState = mockk<NoteState>()

        every { noteItem.switchStatus() } returns noteItem
        mockDeepCopy(noteItem)

        viewModel.noteItem = noteItem
        viewModel.restoreItem = restoreItem
        viewModel.noteState = noteState

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns true
        viewModel.onMenuBind()

        assertEquals(restoreItem, viewModel.restoreItem)

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns false
        viewModel.onMenuBind()

        assertEquals(restoreItem, viewModel.restoreItem)

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        viewModel.onMenuBind()

        assertEquals(restoreItem, viewModel.restoreItem)

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns false
        viewModel.onMenuBind()

        coVerifySequence {
            callback.isDialogOpen
            callback.isDialogOpen
            callback.isDialogOpen
            noteState.isEdit

            callback.isDialogOpen
            noteState.isEdit
            noteItem.switchStatus()
            verifyDeepCopy(noteItem)
            noteState.isEdit
            callback.onBindingEdit(noteItem, isEditMode = false)
            interactor.updateNote(noteItem, updateBind = true)
        }

        assertEquals(noteItem, viewModel.restoreItem)
    }

    @Test fun onMenuConvert() {
        val noteState = mockk<NoteState>()

        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        viewModel.onMenuConvert()

        every { noteState.isEdit } returns true
        viewModel.onMenuConvert()

        verifySequence {
            noteState.isEdit
            callback.showConvertDialog()

            noteState.isEdit
        }
    }

    @Test fun onMenuDelete() {
        TODO()
    }

    @Test fun onMenuEdit() {
        TODO()
    }

    @Test fun onResultSaveControl() {
        TODO()
    }

    @Test fun onInputTextChange() {
        TODO()
    }

    @Test fun onInputRollChange() {
        TODO()
    }

    @Test fun onRollActionNext() {
        TODO()
    }

    @Test fun onTouchGetFlags() {
        TODO()
    }

    @Test fun onTouchSwipe() {
        TODO()
    }

    @Test fun onTouchMove() {
        TODO()
    }

    @Test fun onTouchMoveResult() {
        TODO()
    }


    private fun mockDeepCopy(item: NoteItem.Roll) {
        every { item.id } returns Random.nextLong()
        every { item.create } returns Random.nextString()
        every { item.change } returns Random.nextString()
        every { item.name } returns Random.nextString()
        every { item.text } returns Random.nextString()
        every { item.color } returns Random.nextInt()
        every { item.rankId } returns Random.nextLong()
        every { item.rankPs } returns Random.nextInt()
        every { item.isBin } returns Random.nextBoolean()
        every { item.isStatus } returns Random.nextBoolean()
        every { item.alarmId } returns Random.nextLong()
        every { item.alarmDate } returns Random.nextString()
        every { item.list } returns MutableList(size = 5) { mockk<RollItem>() }

        every {
            item.deepCopy(
                    any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any(), any()
            )
        } returns item
    }

    private fun MockKVerificationScope.verifyDeepCopy(item: NoteItem.Roll) {
        item.id
        item.create
        item.change
        item.name
        item.text
        item.color
        item.rankId
        item.rankPs
        item.isBin
        item.isStatus
        item.alarmId
        item.alarmDate
        item.list

        item.deepCopy(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any()
        )
    }
    
}