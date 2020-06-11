package sgtmelon.scriptum.presentation.screen.vm.impl.note

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.IconState
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteFragment
import kotlin.random.Random

/**
 * Test for [RollNoteViewModel].
 */
@ExperimentalCoroutinesApi
class RollNoteViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IRollNoteFragment
    @MockK lateinit var parentCallback: INoteConnector

    @MockK lateinit var interactor: IRollNoteInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    @MockK lateinit var inputControl: IInputControl
    @MockK lateinit var iconState: IconState

    private val viewModel by lazy { RollNoteViewModel(application) }

    private val fastTest by lazy {
        FastTest.Note.ViewModel(
                callback, parentCallback, interactor, bindInteractor,
                inputControl, iconState, viewModel,
                { mockDeepCopy(it) }, { verifyDeepCopy(it) }
        )
    }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor, bindInteractor)

        viewModel.inputControl = inputControl
        viewModel.iconState = iconState

        assertEquals(NoteData.Default.ID, viewModel.id)
        assertEquals(NoteData.Default.COLOR, viewModel.color)
        assertTrue(viewModel.rankDialogItemArray.isEmpty())
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


    @Test fun cacheData() {
        TODO()
    }

    @Test fun onSetup() {
        TODO()
    }

    @Test fun isNoteInitialized() {
        TODO()
    }


    @Test fun onSaveData() = fastTest.onSaveData()

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


    @Test fun onResultColorDialog() = fastTest.onResultColorDialog(mockk())

    @Test fun onResultRankDialog() = fastTest.onResultRankDialog(mockk())

    @Test fun onResultDateDialog() = fastTest.onResultDateDialog()

    @Test fun onResultDateDialogClear() = fastTest.onResultDateDialogClear(mockk(), mockk())

    @Test fun onResultTimeDialog() = fastTest.onResultTimeDialog(mockk(), mockk())

    @Test fun onResultConvertDialog() = fastTest.onResultConvertDialog(mockk())


    @Test fun onReceiveUnbindNote() = fastTest.onReceiveUnbindNote(mockk(), mockk())


    @Test fun onMenuRestore() = fastTest.onMenuRestore(mockk())

    @Test fun onMenuRestoreOpen() {
        TODO()
    }

    @Test fun onMenuClear() = fastTest.onMenuClear(mockk())


    @Test fun onMenuUndo() {
        TODO()
    }

    @Test fun onMenuRedo() {
        TODO()
    }

    @Test fun onMenuUndoRedo() {
        TODO()
    }

    @Test fun onMenuRank() = fastTest.onMenuRank(mockk())

    @Test fun onMenuColor() = fastTest.onMenuColor(mockk())

    @Test fun onMenuSave() {
        TODO()
    }

    @Test fun onMenuNotification() = fastTest.onMenuNotification(mockk())

    @Test fun onMenuBind() = fastTest.onMenuBind(mockk(), mockk())

    @Test fun onMenuConvert() = fastTest.onMenuConvert()

    @Test fun onMenuDelete() = fastTest.onMenuDelete(mockk())

    @Test fun onMenuEdit() {
        TODO()
    }

    @Test fun setupEditMode() {
        TODO()
    }


    @Test fun onResultSaveControl() {
        TODO()
    }

    @Test fun onInputTextChange() = fastTest.onInputTextChange(mockk())

    @Test fun onInputRollChange() {
        val p = Random.nextInt()
        val text = Random.nextString()

        val noteItem = mockk<NoteItem.Roll>()
        val list = MutableList<RollItem>(size = 5) { mockk() }
        val correctPosition = list.indices.random()
        val item = list[correctPosition]
        val newList = mockk<MutableList<RollItem>>()
        val access = mockk<InputControl.Access>()

        viewModel.noteItem = noteItem

        mockkObject(RollNoteViewModel)

        every { RollNoteViewModel.getCorrectPosition(p, noteItem) } returns -1
        every { noteItem.list } returns list
        every { RollNoteViewModel.getList(noteItem) } returns newList
        every { inputControl.access } returns access

        viewModel.onInputRollChange(p, text)

        every { RollNoteViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { item.text = text } returns Unit

        viewModel.onInputRollChange(p, text)

        verifySequence {
            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list

            RollNoteViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)


            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            item.text = text

            RollNoteViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)
        }
    }

    @Test fun onRollActionNext() {
        viewModel.onRollActionNext()

        verifySequence {
            callback.onFocusEnter()
        }
    }


    @Test fun onTouchAction() {
        val inAction = Random.nextBoolean()

        viewModel.onTouchAction(inAction)

        verifySequence {
            callback.setTouchAction(inAction)
        }
    }

    @Test fun onTouchGetDrag() {
        val noteState = mockk<NoteState>()

        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        assertFalse(viewModel.onTouchGetDrag())

        every { noteState.isEdit } returns true
        assertTrue(viewModel.onTouchGetDrag())

        verifySequence {
            noteState.isEdit
            noteState.isEdit
        }
    }

    @Test fun onTouchGetSwipe() {
        val noteState = mockk<NoteState>()

        viewModel.noteState = noteState

        every { noteState.isEdit } returns false
        assertFalse(viewModel.onTouchGetSwipe())

        every { noteState.isEdit } returns true
        assertTrue(viewModel.onTouchGetSwipe())

        verifySequence {
            noteState.isEdit
            noteState.isEdit
        }
    }

    @Test fun onTouchSwiped() {
        val p = Random.nextInt()
        val correctPosition = Random.nextInt()
        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val newList = mockk<MutableList<RollItem>>()
        val item = mockk<RollItem>()
        val itemJson = Random.nextString()

        val inputAccess = mockk<InputControl.Access>()

        viewModel.noteItem = noteItem

        FastMock.listExtension()
        mockkObject(RollNoteViewModel)

        every { RollNoteViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { noteItem.list } returns list

        every { list.removeAtOrNull(correctPosition) } returns null

        viewModel.onTouchSwiped(p)

        every { list.removeAtOrNull(correctPosition) } returns item
        every { item.toJson() } returns itemJson
        every { inputControl.access } returns inputAccess
        every { RollNoteViewModel.getList(noteItem) } returns newList

        viewModel.onTouchSwiped(p)

        verifySequence {
            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.removeAtOrNull(correctPosition)

            RollNoteViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.removeAtOrNull(correctPosition)
            item.toJson()
            inputControl.onRollRemove(correctPosition, itemJson)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
            RollNoteViewModel.getList(noteItem)
            callback.notifyItemRemoved(newList, p)
        }
    }

    @Test fun onTouchMove() {
        val from = Random.nextInt()
        val correctFrom = Random.nextInt()
        val to = Random.nextInt()
        val correctTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val newList = mockk<MutableList<RollItem>>()

        viewModel.noteItem = noteItem

        FastMock.listExtension()
        mockkObject(RollNoteViewModel)

        every { RollNoteViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { RollNoteViewModel.getCorrectPosition(to, noteItem) } returns correctTo

        every { noteItem.list } returns list
        every { list.move(correctFrom, correctTo) } returns Unit

        every { RollNoteViewModel.getList(noteItem) } returns newList

        assertTrue(viewModel.onTouchMove(from, to))

        verifySequence {
            RollNoteViewModel.getCorrectPosition(from, noteItem)
            RollNoteViewModel.getCorrectPosition(to, noteItem)

            noteItem.list
            list.move(correctFrom, correctTo)

            RollNoteViewModel.getList(noteItem)
        }
    }

    @Test fun onTouchMoveResult() {
        val from = Random.nextInt()
        val correctFrom = Random.nextInt()
        val to = Random.nextInt()
        val correctTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val inputAccess = mockk<InputControl.Access>()

        viewModel.noteItem = noteItem

        mockkObject(RollNoteViewModel)
        every { RollNoteViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { RollNoteViewModel.getCorrectPosition(to, noteItem) } returns correctTo
        every { inputControl.access } returns inputAccess

        viewModel.onTouchMoveResult(from, to)

        verifySequence {
            RollNoteViewModel.getCorrectPosition(from, noteItem)
            RollNoteViewModel.getCorrectPosition(to, noteItem)

            inputControl.onRollMove(correctFrom, correctTo)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
        }
    }



    @Test fun notifyListByVisible() {
        TODO()
    }

    @Test fun getCorrectPosition() {
        TODO()
    }

    @Test fun hide() {
        TODO()
    }

    @Test fun getList() {
        TODO()
    }


    private fun mockDeepCopy(item: NoteItem.Roll) {
        val list = MutableList(size = 5) { mockk<RollItem>() }

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
        every { item.list } returns list

        list.forEach {
            every { it.copy(any(), any(), any(), any()) } returns it
        }

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

        item.deepCopy(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any()
        )
    }

}