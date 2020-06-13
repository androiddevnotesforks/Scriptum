package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.view.inputmethod.EditorInfo
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
        assertTrue(viewModel.isVisible)

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
        assertFalse(spyViewModel.onRestoreData())

        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        val restoreItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        val rollList = mockk<MutableList<RollItem>>()

        val id = Random.nextLong()
        val colorFrom = Random.nextInt()
        val colorTo = Random.nextInt()

        every { noteItem.color } returns colorFrom
        every { spyViewModel.setupEditMode(isEdit = false) } returns Unit
        every { spyViewModel.onUpdateInfo() } returns Unit
        every { spyViewModel.getList(restoreItem) } returns rollList

        mockDeepCopy(restoreItem, color = colorTo)

        spyViewModel.id = id
        spyViewModel.noteItem = noteItem
        spyViewModel.restoreItem = restoreItem

        assertTrue(spyViewModel.onRestoreData())

        verifySequence {
            spyViewModel.onRestoreData()
            spyViewModel.id

            spyViewModel.id = id
            spyViewModel.noteItem = noteItem
            spyViewModel.restoreItem = restoreItem
            spyViewModel.onRestoreData()

            spyViewModel.id
            spyViewModel.noteItem
            noteItem.color
            spyViewModel.restoreItem
            verifyDeepCopy(restoreItem)
            spyViewModel.noteItem = restoreItem
            spyViewModel.noteItem
            restoreItem.color

            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(restoreItem)
            callback.notifyDataSetChanged(rollList)

            spyViewModel.setupEditMode(isEdit = false)
            spyViewModel.onUpdateInfo()

            spyViewModel.callback
            callback.tintToolbar(colorFrom, colorTo)
            spyViewModel.parentCallback
            parentCallback.onUpdateNoteColor(colorTo)
            spyViewModel.inputControl
            inputControl.reset()
        }
    }


    @Test fun onClickVisible_onEdit() {
        val visibleFrom = Random.nextBoolean()
        val visibleTo = !visibleFrom

        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>()
        val id = Random.nextLong()

        every { spyViewModel.notifyListByVisible() } returns Unit
        every { noteState.isCreate } returns false
        every { noteItem.id } returns id

        spyViewModel.isVisible = visibleFrom
        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem

        spyViewModel.onClickVisible()

        coVerifyOrder {
            spyViewModel.isVisible = visibleTo

            callback.setToolbarVisibleIcon(visibleTo, needAnim = true)
            spyViewModel.notifyListByVisible()
            noteState.isCreate
            noteItem.id
            interactor.setVisible(id, visibleTo)
        }
    }

    @Test fun onClickVisible_onCreate() {
        val visibleFrom = Random.nextBoolean()
        val visibleTo = !visibleFrom

        val noteState = mockk<NoteState>(relaxUnitFun = true)

        every { spyViewModel.notifyListByVisible() } returns Unit
        every { noteState.isCreate } returns true

        spyViewModel.isVisible = visibleFrom
        spyViewModel.noteState = noteState

        spyViewModel.onClickVisible()

        coVerifyOrder {
            spyViewModel.isVisible = visibleTo

            callback.setToolbarVisibleIcon(visibleTo, needAnim = true)
            spyViewModel.notifyListByVisible()
            noteState.isCreate
        }
    }

    @Test fun onUpdateInfo() {
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        val list = mockk<MutableList<RollItem>>(relaxUnitFun = true)
        val hideList = mockk<MutableList<RollItem>>(relaxUnitFun = true)

        every { noteItem.list } returns list
        every { spyViewModel.hide(list) } returns hideList

        spyViewModel.noteItem = noteItem
        spyViewModel.isVisible = false

        every { list.size } returns 1
        every { hideList.size } returns 1
        spyViewModel.onUpdateInfo()

        every { hideList.size } returns 0
        spyViewModel.onUpdateInfo()

        every { list.size } returns 0
        spyViewModel.isVisible = true
        spyViewModel.onUpdateInfo()

        every { list.size } returns 0
        spyViewModel.isVisible = false
        every { hideList.size } returns 0
        spyViewModel.onUpdateInfo()

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.isVisible = false
            spyViewModel.onUpdateInfo()
            spyViewModel.noteItem
            noteItem.list
            list.size
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.hide(list)
            hideList.size
            spyViewModel.callback
            callback.animateInfoVisible()

            spyViewModel.onUpdateInfo()
            spyViewModel.noteItem
            noteItem.list
            list.size
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.hide(list)
            hideList.size
            spyViewModel.callback
            callback.onBindingInfo(isListEmpty = false, isListHide = true)
            spyViewModel.callback
            callback.animateInfoVisible()

            spyViewModel.isVisible = true
            spyViewModel.onUpdateInfo()
            spyViewModel.noteItem
            noteItem.list
            list.size
            spyViewModel.callback
            callback.onBindingInfo(isListEmpty = true, isListHide = false)
            spyViewModel.callback
            callback.animateInfoVisible()

            spyViewModel.isVisible = false
            spyViewModel.onUpdateInfo()
            spyViewModel.noteItem
            noteItem.list
            list.size
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.hide(list)
            hideList.size
            spyViewModel.callback
            callback.onBindingInfo(isListEmpty = true, isListHide = true)
            spyViewModel.callback
            callback.animateInfoVisible()
        }
    }

    @Test fun onEditorClick() {
        assertFalse(viewModel.onEditorClick(Random.nextInt()))

        val i = EditorInfo.IME_ACTION_DONE

        every { spyViewModel.onMenuSave(changeMode = true) } returns Random.nextBoolean()
        every { spyViewModel.onClickAdd(simpleClick = true) } returns Unit

        every { callback.getEnterText() } returns Random.nextString()
        assertTrue(spyViewModel.onEditorClick(i))

        every { callback.getEnterText() } returns "   "
        assertTrue(spyViewModel.onEditorClick(i))

        every { callback.getEnterText() } returns ""
        assertTrue(spyViewModel.onEditorClick(i))

        verifySequence {
            spyViewModel.onEditorClick(i)
            spyViewModel.callback
            callback.getEnterText()
            spyViewModel.onClickAdd(simpleClick = true)

            repeat(times = 2) {
                spyViewModel.onEditorClick(i)
                spyViewModel.callback
                callback.getEnterText()
                spyViewModel.onMenuSave(changeMode = true)
            }
        }
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


    @Test fun getCorrectPosition() {
        TODO()
    }

    @Test fun hide() {
        TODO()
    }

    @Test fun getList() {
        TODO()
    }

    @Test fun notifyListByVisible() {
        TODO()
    }


    private fun mockDeepCopy(item: NoteItem.Roll, id: Long = Random.nextLong(),
                             create: String = Random.nextString(),
                             change: String = Random.nextString(),
                             name: String = Random.nextString(),
                             text: String = Random.nextString(),
                             color: Int = Random.nextInt(),
                             rankId: Long = Random.nextLong(),
                             rankPs: Int = Random.nextInt(),
                             isBin: Boolean = Random.nextBoolean(),
                             isStatus: Boolean = Random.nextBoolean(),
                             alarmId: Long = Random.nextLong(),
                             alarmDate: String = Random.nextString()) {
        val list = MutableList(size = 5) { mockk<RollItem>() }

        every { item.id } returns id
        every { item.create } returns create
        every { item.change } returns change
        every { item.name } returns name
        every { item.text } returns text
        every { item.color } returns color
        every { item.rankId } returns rankId
        every { item.rankPs } returns rankPs
        every { item.isBin } returns isBin
        every { item.isStatus } returns isStatus
        every { item.alarmId } returns alarmId
        every { item.alarmDate } returns alarmDate
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