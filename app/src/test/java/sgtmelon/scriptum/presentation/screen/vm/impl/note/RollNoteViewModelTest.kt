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
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
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
    @MockK lateinit var saveControl: ISaveControl

    private val viewModel by lazy { RollNoteViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel, recordPrivateCalls = true) }

    private val fastTest by lazy {
        FastTest.Note.ViewModel(
                callback, parentCallback, interactor, bindInteractor,
                inputControl, viewModel, spyViewModel, { mockDeepCopy(it) },
                { verifyDeepCopy(it) }
        )
    }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor, bindInteractor)

        viewModel.inputControl = inputControl

        assertEquals(NoteData.Default.ID, viewModel.id)
        assertEquals(NoteData.Default.COLOR, viewModel.color)
        assertTrue(viewModel.rankDialogItemArray.isEmpty())

        assertNotNull(viewModel.callback)
        assertNotNull(viewModel.parentCallback)
    }

    override fun tearDown() {
        super.tearDown()

        confirmVerified(
                callback, parentCallback, interactor, bindInteractor, inputControl, saveControl
        )
    }

    @Test override fun onDestroy() = fastTest.onDestroy()


    @Test fun cacheData() = fastTest.cacheData(mockk())

    @Test fun onSetup() {
        TODO()
    }

    @Test fun isNoteInitialized() = fastTest.isNoteInitialized(mockk())


    @Test fun onSaveData() = fastTest.onSaveData()

    @Test fun onResume() = fastTest.onResume()

    @Test fun onPause() = fastTest.onPause()


    @Test fun onClickBackArrow() = fastTest.onClickBackArrow()

    @Test fun onPressBack() = fastTest.onPressBack()

    @Test fun onRestoreData() {
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

    @Test fun onMenuRestoreOpen() = fastTest.onMenuRestoreOpen(mockk())

    @Test fun onMenuClear() = fastTest.onMenuClear(mockk())


    @Test fun onMenuUndo() = fastTest.onMenuUndo()

    @Test fun onMenuRedo() = fastTest.onMenuRedo()

    @Test fun onMenuUndoRedo() {
        TODO()
    }

    @Test fun onMenuUndoRedoRank() = fastTest.onMenuUndoRedoRank(mockk(relaxUnitFun = true))

    @Test fun onMenuUndoRedoColor() = fastTest.onMenuUndoRedoColor(mockk())

    @Test fun onMenuUndoRedoName() = fastTest.onMenuUndoRedoName()

    @Test fun onMenuRank() = fastTest.onMenuRank(mockk())

    @Test fun onMenuColor() = fastTest.onMenuColor(mockk())

    @Test fun onMenuSave() {
        TODO()
    }

    @Test fun onMenuNotification() = fastTest.onMenuNotification(mockk())

    @Test fun onMenuBind() = fastTest.onMenuBind(mockk(), mockk())

    @Test fun onMenuConvert() = fastTest.onMenuConvert()

    @Test fun onMenuDelete() = fastTest.onMenuDelete(mockk())

    @Test fun onMenuEdit() = fastTest.onMenuEdit()

    @Test fun setupEditMode() {
        TODO()
    }


    @Test fun onResultSaveControl() = fastTest.onResultSaveControl()

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

        spyViewModel.noteItem = noteItem

        every { spyViewModel.getCorrectPosition(p, noteItem) } returns -1
        every { noteItem.list } returns list
        every { spyViewModel.getList(noteItem) } returns newList
        every { inputControl.access } returns access

        spyViewModel.onInputRollChange(p, text)

        every { spyViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { item.text = text } returns Unit

        spyViewModel.onInputRollChange(p, text)

        verifyOrder {
            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list

            spyViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)


            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            item.text = text

            spyViewModel.getList(noteItem)
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

        spyViewModel.noteItem = noteItem

        FastMock.listExtension()

        every { spyViewModel.getCorrectPosition(p, noteItem) } returns correctPosition
        every { noteItem.list } returns list

        every { list.removeAtOrNull(correctPosition) } returns null

        spyViewModel.onTouchSwiped(p)

        every { list.removeAtOrNull(correctPosition) } returns item
        every { item.toJson() } returns itemJson
        every { inputControl.access } returns inputAccess
        every { spyViewModel.getList(noteItem) } returns newList

        spyViewModel.onTouchSwiped(p)

        verifyOrder {
            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.removeAtOrNull(correctPosition)

            spyViewModel.getCorrectPosition(p, noteItem)
            noteItem.list
            list.removeAtOrNull(correctPosition)
            item.toJson()
            inputControl.onRollRemove(correctPosition, itemJson)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
            spyViewModel.getList(noteItem)
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

        spyViewModel.noteItem = noteItem

        FastMock.listExtension()

        every { spyViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { spyViewModel.getCorrectPosition(to, noteItem) } returns correctTo

        every { noteItem.list } returns list
        every { list.move(correctFrom, correctTo) } returns Unit

        every { spyViewModel.getList(noteItem) } returns newList

        assertTrue(spyViewModel.onTouchMove(from, to))

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onTouchMove(from, to)

            spyViewModel.noteItem
            spyViewModel.getCorrectPosition(from, noteItem)
            spyViewModel.noteItem
            spyViewModel.getCorrectPosition(to, noteItem)

            spyViewModel.noteItem
            noteItem.list
            list.move(correctFrom, correctTo)

            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyItemMoved(newList, from, to)
        }
    }

    @Test fun onTouchMoveResult() {
        val from = Random.nextInt()
        val correctFrom = Random.nextInt()
        val to = Random.nextInt()
        val correctTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val inputAccess = mockk<InputControl.Access>()

        spyViewModel.noteItem = noteItem

        every { spyViewModel.getCorrectPosition(from, noteItem) } returns correctFrom
        every { spyViewModel.getCorrectPosition(to, noteItem) } returns correctTo
        every { inputControl.access } returns inputAccess

        spyViewModel.onTouchMoveResult(from, to)

        verifyOrder {
            spyViewModel.getCorrectPosition(from, noteItem)
            spyViewModel.getCorrectPosition(to, noteItem)

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