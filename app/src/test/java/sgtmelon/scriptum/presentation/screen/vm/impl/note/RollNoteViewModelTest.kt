package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.view.inputmethod.EditorInfo
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.*
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.hide
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.extension.validIndexOf
import sgtmelon.scriptum.extension.validRemoveAt
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IRollNoteFragment
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
        FastTest.ViewModel(
            callback, parentCallback, interactor, bindInteractor,
            inputControl, viewModel, spyViewModel, { FastMock.Note.deepCopy(it) },
            { verifyDeepCopy(it) }
        )
    }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor, bindInteractor)

        viewModel.inputControl = inputControl

        assertEquals(Note.Default.ID, viewModel.id)
        assertEquals(Note.Default.COLOR, viewModel.color)
        assertTrue(viewModel.mayAnimateIcon)
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

    @Test fun onSetup() = fastTest.onSetup()

    @Test fun getBundleData() = fastTest.getBundleData()

    @Test fun setupBeforeInitialize() {
        val color = Random.nextInt()

        viewModel.color = color
        viewModel.setupBeforeInitialize()

        assertFalse(viewModel.isFirstRun)

        verifySequence {
            callback.apply {
                setupBinding()
                setupToolbar(color)
                setupEnter(inputControl)
                setupRecycler(inputControl, isFirstRun = true)

                showToolbarVisibleIcon(isShow = false)
            }
        }
    }

    @Test fun tryInitializeNote() = startCoTest {
        val name = nextString()
        val itemArray = Array(size = 10) { nextString() }
        val defaultColor = Random.nextInt()
        val noteItem = mockk<NoteItem.Roll>()
        val id = Random.nextLong()
        val isBin = Random.nextBoolean()

        every { spyViewModel.isNoteInitialized() } returns true

        assertTrue(spyViewModel.tryInitializeNote())

        every { spyViewModel.isNoteInitialized() } returns false
        every { parentCallback.getString(R.string.dialog_item_rank) } returns name
        coEvery { interactor.getRankDialogItemArray(name) } returns itemArray
        every { interactor.defaultColor } returns defaultColor
        mockkObject(NoteItem.Roll)
        every { NoteItem.Roll.getCreate(defaultColor) } returns noteItem
        every { spyViewModel.cacheData() } returns Unit

        assertTrue(spyViewModel.tryInitializeNote())

        coEvery { interactor.getItem(id) } returns null

        spyViewModel.id = id
        assertFalse(spyViewModel.tryInitializeNote())

        coEvery { interactor.getItem(id) } returns noteItem
        FastMock.Note.deepCopy(noteItem)
        every { noteItem.isBin } returns isBin

        assertTrue(spyViewModel.tryInitializeNote())

        coVerifySequence {
            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()

            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()
            spyViewModel.parentCallback
            parentCallback.getString(R.string.dialog_item_rank)
            spyViewModel.interactor
            interactor.getRankDialogItemArray(name)
            spyViewModel.rankDialogItemArray = itemArray
            spyViewModel.id
            spyViewModel.interactor
            interactor.defaultColor
            NoteItem.Roll.getCreate(defaultColor)
            spyViewModel.noteItem = noteItem
            spyViewModel.cacheData()
            spyViewModel.noteState = NoteState(isCreate = true)

            spyViewModel.id = id
            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()
            spyViewModel.parentCallback
            parentCallback.getString(R.string.dialog_item_rank)
            spyViewModel.interactor
            interactor.getRankDialogItemArray(name)
            spyViewModel.rankDialogItemArray = itemArray
            spyViewModel.id
            spyViewModel.interactor
            spyViewModel.id
            interactor.getItem(id)
            spyViewModel.parentCallback
            parentCallback.finish()

            spyViewModel.tryInitializeNote()
            spyViewModel.isNoteInitialized()
            spyViewModel.parentCallback
            parentCallback.getString(R.string.dialog_item_rank)
            spyViewModel.interactor
            interactor.getRankDialogItemArray(name)
            spyViewModel.rankDialogItemArray = itemArray
            spyViewModel.id
            spyViewModel.interactor
            spyViewModel.id
            interactor.getItem(id)
            spyViewModel.noteItem = noteItem
            verifyDeepCopy(noteItem)
            spyViewModel.restoreItem = noteItem
            spyViewModel.noteItem
            noteItem.isBin
            spyViewModel.noteState = NoteState(isBin = isBin)
        }
    }

    @Test fun setupAfterInitialize() = startCoTest {
        val noteItem = mockk<NoteItem.Roll>()
        val rollList = mockk<MutableList<RollItem>>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)

        val isVisible = Random.nextBoolean()
        val isRankEmpty = Random.nextBoolean()
        val rankDialogItemArray = if (isRankEmpty) {
            arrayOf(nextString())
        } else {
            arrayOf(nextString(), nextString())
        }
        val isEdit = Random.nextBoolean()

        spyViewModel.noteItem = noteItem
        spyViewModel.rankDialogItemArray = rankDialogItemArray
        spyViewModel.noteState = noteState

        every { spyViewModel.getList(noteItem) } returns rollList
        every { spyViewModel.onUpdateInfo() } returns Unit
        every { noteState.isEdit } returns isEdit
        every { spyViewModel.setupEditMode(isEdit) } returns Unit
        every { noteItem.isVisible } returns isVisible

        spyViewModel.setupAfterInitialize()

        coVerifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.rankDialogItemArray = rankDialogItemArray
            spyViewModel.noteState = noteState
            spyViewModel.setupAfterInitialize()

            spyViewModel.callback
            spyViewModel.rankDialogItemArray
            callback.setupDialog(rankDialogItemArray)
            spyViewModel.callback
            callback.setupProgress()

            spyViewModel.mayAnimateIcon = false
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.setupEditMode(isEdit)
            spyViewModel.mayAnimateIcon = true

            spyViewModel.callback
            callback.showToolbarVisibleIcon(isShow = true)
            spyViewModel.noteItem
            noteItem.isVisible
            callback.setToolbarVisibleIcon(isVisible, needAnim = false)
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyDataSetChanged(rollList)

            spyViewModel.onUpdateInfo()

            spyViewModel.callback
            spyViewModel.rankDialogItemArray
            callback.onBindingLoad(isRankEmpty)
        }
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
        val isVisible = Random.nextBoolean()
        val colorTo = Random.nextInt()

        every { noteItem.color } returns colorFrom
        every { noteItem.isVisible } returns isVisible
        every { spyViewModel.setupEditMode(isEdit = false) } returns Unit
        every { spyViewModel.onUpdateInfo() } returns Unit
        every { spyViewModel.getList(restoreItem) } returns rollList

        FastMock.Note.deepCopy(restoreItem, color = colorTo)

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
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.restoreItem
            verifyDeepCopy(restoreItem, isVisible = isVisible)
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
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)
        val isEdit = Random.nextBoolean()

        every { noteItem.isVisible } returns visibleFrom
        every { spyViewModel.notifyListByVisible() } returns Unit
        every { noteState.isCreate } returns false
        every { noteState.isEdit } returns isEdit

        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem
        spyViewModel.onClickVisible()

        coVerifyOrder {
            spyViewModel.noteState = noteState
            spyViewModel.noteItem = noteItem
            spyViewModel.onClickVisible()

            spyViewModel.noteItem
            spyViewModel.noteItem
            noteItem.isVisible
            noteItem.isVisible = visibleTo

            spyViewModel.callback
            spyViewModel.noteItem
            noteItem.isVisible
            /**
             * Here set visibleFrom (not visibleTo) because [NoteItem.Roll.isVisible] mock to
             * return visibleFrom.
             */
            callback.setToolbarVisibleIcon(visibleFrom, needAnim = true)
            spyViewModel.notifyListByVisible()
            spyViewModel.noteState
            noteState.isCreate

            spyViewModel.interactor
            spyViewModel.noteItem
            spyViewModel.noteState
            noteState.isEdit
            interactor.setVisible(noteItem, !isEdit)
        }
    }

    @Test fun onClickVisible_onCreate() {
        val visibleFrom = Random.nextBoolean()
        val visibleTo = !visibleFrom

        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)

        every { noteItem.isVisible } returns visibleFrom
        every { spyViewModel.notifyListByVisible() } returns Unit
        every { noteState.isCreate } returns true


        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem
        spyViewModel.onClickVisible()

        coVerifySequence {
            spyViewModel.noteState = noteState
            spyViewModel.noteItem = noteItem
            spyViewModel.onClickVisible()

            spyViewModel.noteItem
            spyViewModel.noteItem
            noteItem.isVisible
            noteItem.isVisible = visibleTo

            spyViewModel.callback
            spyViewModel.noteItem
            noteItem.isVisible
            /**
             * Here set visibleFrom (not visibleTo) because [NoteItem.Roll.isVisible] mock to
             * return visibleFrom.
             */
            callback.setToolbarVisibleIcon(visibleFrom, needAnim = true)
            spyViewModel.notifyListByVisible()
            spyViewModel.noteState
            noteState.isCreate
        }
    }

    @Test fun onUpdateInfo() {
        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val hideList = mockk<MutableList<RollItem>>()

        FastMock.listExtension()

        every { noteItem.list } returns list
        every { list.hide() } returns hideList

        viewModel.noteItem = noteItem

        every { list.size } returns 1
        every { noteItem.isVisible } returns false
        every { hideList.size } returns 1
        viewModel.onUpdateInfo()

        every { hideList.size } returns 0
        viewModel.onUpdateInfo()

        every { list.size } returns 0
        every { noteItem.isVisible } returns true
        viewModel.onUpdateInfo()

        every { list.size } returns 0
        every { noteItem.isVisible } returns false
        every { hideList.size } returns 0
        viewModel.onUpdateInfo()

        verifySequence {
            noteItem.list
            list.size
            noteItem.isVisible
            noteItem.list
            list.hide()
            hideList.size
            callback.animateInfoVisible()

            noteItem.list
            list.size
            noteItem.isVisible
            noteItem.list
            list.hide()
            hideList.size
            callback.onBindingInfo(isListEmpty = false, isListHide = true)
            callback.animateInfoVisible()

            noteItem.list
            list.size
            noteItem.isVisible
            callback.onBindingInfo(isListEmpty = true, isListHide = false)
            callback.animateInfoVisible()

            noteItem.list
            list.size
            noteItem.isVisible
            noteItem.list
            list.hide()
            hideList.size
            callback.onBindingInfo(isListEmpty = true, isListHide = true)
            callback.animateInfoVisible()
        }
    }

    @Test fun onEditorClick() {
        assertFalse(viewModel.onEditorClick(Random.nextInt()))

        val i = EditorInfo.IME_ACTION_DONE

        every { spyViewModel.onMenuSave(changeMode = true) } returns Random.nextBoolean()
        every { spyViewModel.onClickAdd(simpleClick = true) } returns Unit

        every { callback.getEnterText() } returns nextString()
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

    @Test fun onClickAdd_onEmptyText() {
        val noteState = mockk<NoteState>(relaxUnitFun = true)

        viewModel.noteState = noteState

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns false
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.isDialogOpen } returns true
        every { noteState.isEdit } returns true
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns false
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.getEnterText() } returns "   "
        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        viewModel.onClickAdd(Random.nextBoolean())

        every { callback.getEnterText() } returns ""
        viewModel.onClickAdd(Random.nextBoolean())

        verifySequence {
            callback.isDialogOpen
            callback.isDialogOpen
            callback.isDialogOpen
            noteState.isEdit

            callback.isDialogOpen
            noteState.isEdit
            callback.getEnterText()

            callback.isDialogOpen
            noteState.isEdit
            callback.getEnterText()
        }
    }

    @Test fun onClickAdd_onNormalText() {
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)

        val enterText = nextString()
        val optimalList = mockk<MutableList<RollItem>>()
        val normalList = mockk<MutableList<RollItem>>(relaxUnitFun = true)
        val size = Random.nextInt()
        val access = mockk<InputControl.Access>()

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        every { callback.getEnterText() } returns enterText
        every { noteItem.list } returns normalList
        every { normalList.size } returns size
        every { inputControl.access } returns access
        every { spyViewModel.getList(noteItem) } returns optimalList

        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem

        spyViewModel.onClickAdd(simpleClick = false)
        spyViewModel.onClickAdd(simpleClick = true)

        verifySequence {
            spyViewModel.noteState = noteState
            spyViewModel.noteItem = noteItem

            spyViewModel.onClickAdd(simpleClick = false)
            spyViewModel.callback
            callback.isDialogOpen
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.callback
            callback.getEnterText()
            spyViewModel.callback
            callback.clearEnterText()
            val firstRollItem = RollItem(position = 0, text = enterText)
            spyViewModel.inputControl
            inputControl.onRollAdd(p = 0, valueTo = firstRollItem.toJson())
            spyViewModel.noteItem
            noteItem.list
            normalList.add(0, firstRollItem)
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.scrollToItem(simpleClick = false, p = 0, list = optimalList)


            spyViewModel.onClickAdd(simpleClick = true)
            spyViewModel.callback
            callback.isDialogOpen
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.callback
            callback.getEnterText()
            spyViewModel.callback
            callback.clearEnterText()
            val secondRollItem = RollItem(position = size, text = enterText)
            spyViewModel.noteItem
            noteItem.list
            normalList.size
            spyViewModel.inputControl
            inputControl.onRollAdd(p = size, valueTo = secondRollItem.toJson())
            spyViewModel.noteItem
            noteItem.list
            normalList.add(size, secondRollItem)
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.scrollToItem(simpleClick = true, p = size, list = optimalList)
        }
    }

    @Test fun onClickItemCheck() {
        val p = Random.nextInt()
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)

        val absolutePosition = Random.nextInt()
        val optimalList = mockk<MutableList<RollItem>>()
        val normalList = mockk<MutableList<RollItem>>()
        val check = Random.nextInt()
        val size = Random.nextInt()

        every { spyViewModel.getAbsolutePosition(p) } returns absolutePosition
        every { spyViewModel.getList(noteItem) } returns optimalList
        every { spyViewModel.cacheData() } returns Unit
        every { noteItem.getCheck() } returns check
        every { noteItem.list } returns normalList
        every { normalList.size } returns size

        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem

        every { noteState.isEdit } returns true
        spyViewModel.onClickItemCheck(p)

        every { noteState.isEdit } returns false
        every { noteItem.isVisible } returns false
        spyViewModel.onClickItemCheck(p)

        every { noteItem.isVisible } returns true
        spyViewModel.onClickItemCheck(p)

        coVerifyOrder {
            spyViewModel.noteState = noteState
            spyViewModel.noteItem = noteItem

            spyViewModel.onClickItemCheck(p)
            spyViewModel.noteState
            noteState.isEdit


            spyViewModel.onClickItemCheck(p)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            spyViewModel.getAbsolutePosition(p)
            spyViewModel.noteItem
            noteItem.onItemCheck(absolutePosition)
            spyViewModel.cacheData()

            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyItemRemoved(optimalList, p)

            spyViewModel.noteItem
            spyViewModel.callback
            noteItem.getCheck()
            noteItem.list
            normalList.size
            callback.updateProgress(check, size)

            spyViewModel.noteItem
            interactor.updateRollCheck(noteItem, absolutePosition)


            spyViewModel.onClickItemCheck(p)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            spyViewModel.getAbsolutePosition(p)
            spyViewModel.noteItem
            noteItem.onItemCheck(absolutePosition)
            spyViewModel.cacheData()

            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyItemChanged(optimalList, p)

            spyViewModel.noteItem
            spyViewModel.callback
            noteItem.getCheck()
            noteItem.list
            normalList.size
            callback.updateProgress(check, size)

            spyViewModel.noteItem
            interactor.updateRollCheck(noteItem, absolutePosition)
        }
    }

    @Test fun onLongClickItemCheck() {
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)

        val isCheck = Random.nextBoolean()
        val optimalList = mockk<MutableList<RollItem>>()
        val normalList = mockk<MutableList<RollItem>>()
        val check = Random.nextInt()
        val size = Random.nextInt()

        every { noteItem.onItemLongCheck() } returns isCheck
        every { spyViewModel.cacheData() } returns Unit
        every { spyViewModel.getList(noteItem) } returns optimalList
        every { noteItem.getCheck() } returns check
        every { noteItem.list } returns normalList
        every { normalList.size } returns size
        every { spyViewModel.notifyListByVisible() } returns Unit

        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem

        every { noteState.isEdit } returns true
        spyViewModel.onLongClickItemCheck()

        every { noteState.isEdit } returns false
        every { noteItem.isVisible } returns false
        spyViewModel.onLongClickItemCheck()

        every { noteItem.isVisible } returns true
        spyViewModel.onLongClickItemCheck()

        coVerifyOrder {
            spyViewModel.noteState = noteState
            spyViewModel.noteItem = noteItem

            spyViewModel.onLongClickItemCheck()
            spyViewModel.noteState
            noteState.isEdit


            spyViewModel.onLongClickItemCheck()
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.onItemLongCheck()
            spyViewModel.cacheData()

            spyViewModel.callback
            callback.changeCheckToggle(state = true)
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyDataRangeChanged(optimalList)
            callback.changeCheckToggle(state = false)
            spyViewModel.noteItem
            noteItem.getCheck()
            noteItem.list
            normalList.size
            callback.updateProgress(check, size)

            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.notifyListByVisible()
            spyViewModel.interactor
            spyViewModel.noteItem
            interactor.updateRollCheck(noteItem, isCheck)


            spyViewModel.onLongClickItemCheck()
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.onItemLongCheck()
            spyViewModel.cacheData()

            spyViewModel.callback
            callback.changeCheckToggle(state = true)
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyDataRangeChanged(optimalList)
            callback.changeCheckToggle(state = false)
            spyViewModel.noteItem
            noteItem.getCheck()
            noteItem.list
            normalList.size
            callback.updateProgress(check, size)

            spyViewModel.interactor
            spyViewModel.noteItem
            interactor.updateRollCheck(noteItem, isCheck)
        }
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

    @Test fun onMenuUndoRedo() = fastTest.onMenuUndoRedo(mockk())

    @Test fun onMenuUndoRedoSelect() {
        val item = mockk<InputItem>()
        val isUndo = Random.nextBoolean()

        every { item.tag } returns InputAction.RANK
        every { spyViewModel.onMenuUndoRedoRank(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.COLOR
        every { spyViewModel.onMenuUndoRedoColor(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.NAME
        every { spyViewModel.onMenuUndoRedoName(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.ROLL_ADD
        every { spyViewModel.onMenuUndoRedoRemove(item, isUndo = false) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo = false)

        every { item.tag } returns InputAction.ROLL_ADD
        every { spyViewModel.onMenuUndoRedoAdd(item) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo = true)

        every { item.tag } returns InputAction.ROLL_REMOVE
        spyViewModel.onMenuUndoRedoSelect(item, isUndo = false)

        every { item.tag } returns InputAction.ROLL_REMOVE
        every { spyViewModel.onMenuUndoRedoRemove(item, isUndo = true) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo = true)

        every { item.tag } returns InputAction.ROLL_MOVE
        every { spyViewModel.onMenuUndoRedoMove(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        verifySequence {
            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoRank(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoColor(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoName(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo = false)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoRemove(item, isUndo = false)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo = true)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoAdd(item)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo = false)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoAdd(item)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo = true)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoRemove(item, isUndo = true)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoMove(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true
        }
    }

    @Test fun onMenuUndoRedoRank() = fastTest.onMenuUndoRedoRank(mockk(relaxUnitFun = true))

    @Test fun onMenuUndoRedoColor() = fastTest.onMenuUndoRedoColor(mockk())

    @Test fun onMenuUndoRedoName() = fastTest.onMenuUndoRedoName()

    @Test fun onMenuUndoRedoRoll() {
        val item = mockk<InputItem>()
        val isUndo = Random.nextBoolean()

        val noteItem = mockk<NoteItem.Roll>()
        val size = getRandomSize()
        val list = MutableList<RollItem>(size) { mockk() }
        val p = list.indices.random()
        val rollItem = list[p]
        val returnList = mockk<MutableList<RollItem>>()
        val validIndex = Random.nextInt()
        val text = nextString()
        val cursor = mockk<InputItem.Cursor>()
        val cursorPosition = Random.nextInt()

        FastMock.listExtension()

        every { item.p } returns -1
        every { noteItem.list } returns list

        spyViewModel.noteItem = noteItem
        spyViewModel.onMenuUndoRedoRoll(item, isUndo)

        every { item.p } returns p
        every { spyViewModel.getList(noteItem) } returns returnList
        every { returnList.validIndexOf(rollItem) } returns null

        spyViewModel.onMenuUndoRedoRoll(item, isUndo)

        every { returnList.validIndexOf(rollItem) } returns validIndex
        every { item[isUndo] } returns text
        every { rollItem.text = text } returns Unit
        every { noteItem.isVisible } returns true
        every { item.cursor } returns cursor
        every { cursor[isUndo] } returns cursorPosition

        spyViewModel.onMenuUndoRedoRoll(item, isUndo)

        every { noteItem.isVisible } returns false
        every { rollItem.isCheck } returns true

        spyViewModel.onMenuUndoRedoRoll(item, isUndo)

        every { rollItem.isCheck } returns false

        spyViewModel.onMenuUndoRedoRoll(item, isUndo)

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onMenuUndoRedoRoll(item, isUndo)

            spyViewModel.noteItem
            noteItem.list
            item.p

            spyViewModel.onMenuUndoRedoRoll(item, isUndo)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)

            spyViewModel.onMenuUndoRedoRoll(item, isUndo)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)
            item[isUndo]
            rollItem.text = text
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            item.cursor
            cursor[isUndo]
            callback.notifyItemChanged(returnList, validIndex, cursorPosition)

            spyViewModel.onMenuUndoRedoRoll(item, isUndo)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)
            item[isUndo]
            rollItem.text = text
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.noteItem
            noteItem.isVisible
            rollItem.isCheck

            spyViewModel.onMenuUndoRedoRoll(item, isUndo)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)
            item[isUndo]
            rollItem.text = text
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.noteItem
            noteItem.isVisible
            rollItem.isCheck
            spyViewModel.callback
            item.cursor
            cursor[isUndo]
            callback.notifyItemChanged(returnList, validIndex, cursorPosition)
        }
    }

    @Test fun onMenuUndoRedoAdd() {
        val item = mockk<InputItem>()

        val noteItem = mockk<NoteItem.Roll>()
        val size = getRandomSize()
        val list = MutableList<RollItem>(size) { mockk() }
        val p = list.indices.random()
        val rollItem = list[p]
        val returnList = mockk<MutableList<RollItem>>()
        val validIndex = Random.nextInt()

        FastMock.listExtension()

        every { item.p } returns -1
        every { noteItem.list } returns list

        spyViewModel.noteItem = noteItem
        spyViewModel.onMenuUndoRedoAdd(item)

        every { item.p } returns p
        every { spyViewModel.getList(noteItem) } returns returnList
        every { returnList.validIndexOf(rollItem) } returns null

        spyViewModel.onMenuUndoRedoAdd(item)

        every { returnList.validIndexOf(rollItem) } returns validIndex
        every { list.validRemoveAt(p) } returns null

        spyViewModel.onMenuUndoRedoAdd(item)

        every { list.validRemoveAt(p) } returns rollItem
        every { noteItem.isVisible } returns true

        spyViewModel.onMenuUndoRedoAdd(item)

        every { noteItem.isVisible } returns false
        every { rollItem.isCheck } returns true

        spyViewModel.onMenuUndoRedoAdd(item)

        every { rollItem.isCheck } returns false

        spyViewModel.onMenuUndoRedoAdd(item)

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onMenuUndoRedoAdd(item)

            spyViewModel.noteItem
            noteItem.list
            item.p

            spyViewModel.onMenuUndoRedoAdd(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)

            spyViewModel.onMenuUndoRedoAdd(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.validRemoveAt(p)

            spyViewModel.onMenuUndoRedoAdd(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.validRemoveAt(p)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyItemRemoved(returnList, validIndex)

            spyViewModel.onMenuUndoRedoAdd(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.validRemoveAt(p)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.noteItem
            noteItem.isVisible
            rollItem.isCheck

            spyViewModel.onMenuUndoRedoAdd(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.validRemoveAt(p)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.noteItem
            noteItem.isVisible
            rollItem.isCheck
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyItemRemoved(returnList, validIndex)
        }
    }

    @Test fun onMenuUndoRedoRemove() {
        TODO()
    }

    @Test fun onMenuUndoRedoMove() {
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

    @Test fun onMenuEdit() = fastTest.onMenuEdit()

    @Test fun setupEditMode() {
        TODO()
    }


    @Test fun onResultSaveControl() = fastTest.onResultSaveControl()

    @Test fun onInputTextChange() = fastTest.onInputTextChange(mockk())

    @Test fun onInputRollChange() {
        val p = Random.nextInt()
        val text = nextString()

        val noteItem = mockk<NoteItem.Roll>()
        val list = MutableList<RollItem>(size = 5) { mockk() }
        val absolutePosition = list.indices.random()
        val item = list[absolutePosition]
        val newList = mockk<MutableList<RollItem>>()
        val access = mockk<InputControl.Access>()

        spyViewModel.noteItem = noteItem

        every { spyViewModel.getAbsolutePosition(p) } returns -1
        every { noteItem.list } returns list
        every { spyViewModel.getList(noteItem) } returns newList
        every { inputControl.access } returns access

        spyViewModel.onInputRollChange(p, text)

        every { spyViewModel.getAbsolutePosition(p) } returns absolutePosition
        every { item.text = text } returns Unit

        spyViewModel.onInputRollChange(p, text)

        verifyOrder {
            spyViewModel.getAbsolutePosition(p)
            noteItem.list

            spyViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)


            spyViewModel.getAbsolutePosition(p)
            noteItem.list
            item.text = text

            spyViewModel.getList(noteItem)
            callback.setList(newList)
            inputControl.access
            callback.onBindingInput(noteItem, access)
        }
    }

    @Test fun getAbsolutePosition() {
        val item = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val hideList = mockk<MutableList<RollItem>>()
        val p = Random.nextInt()
        val index = Random.nextInt()
        val rollItem = mockk<RollItem>()

        FastMock.listExtension()

        every { item.isVisible } returns true

        viewModel.noteItem = item
        assertEquals(p, viewModel.getAbsolutePosition(p))

        every { item.isVisible } returns false
        every { item.list } returns list
        every { list.hide() } returns hideList
        every { hideList[p] } returns rollItem
        every { list.indexOf(rollItem) } returns index

        assertEquals(index, viewModel.getAbsolutePosition(p))

        verifySequence {
            item.isVisible

            item.isVisible
            item.list
            list.hide()
            hideList[p]
            list.indexOf(rollItem)
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
        assertFalse(viewModel.onTouchGetDrag(mayDrag = false))
        assertFalse(viewModel.onTouchGetDrag(mayDrag = true))

        every { noteState.isEdit } returns true
        assertFalse(viewModel.onTouchGetDrag(mayDrag = false))
        assertTrue(viewModel.onTouchGetDrag(mayDrag = true))

        verifySequence {
            noteState.isEdit
            noteState.isEdit
            noteState.isEdit

            noteState.isEdit
            callback.hideKeyboard()
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
        val absolutePosition = Random.nextInt()
        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val newList = mockk<MutableList<RollItem>>()
        val item = mockk<RollItem>()
        val itemJson = nextString()

        val inputAccess = mockk<InputControl.Access>()

        spyViewModel.noteItem = noteItem

        FastMock.listExtension()

        every { spyViewModel.getAbsolutePosition(p) } returns absolutePosition
        every { noteItem.list } returns list

        every { list.validRemoveAt(absolutePosition) } returns null

        spyViewModel.onTouchSwiped(p)

        every { list.validRemoveAt(absolutePosition) } returns item
        every { item.toJson() } returns itemJson
        every { inputControl.access } returns inputAccess
        every { spyViewModel.getList(noteItem) } returns newList

        spyViewModel.onTouchSwiped(p)

        verifyOrder {
            spyViewModel.getAbsolutePosition(p)
            noteItem.list
            list.validRemoveAt(absolutePosition)

            spyViewModel.getAbsolutePosition(p)
            noteItem.list
            list.validRemoveAt(absolutePosition)
            item.toJson()
            inputControl.onRollRemove(absolutePosition, itemJson)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
            spyViewModel.getList(noteItem)
            callback.notifyItemRemoved(newList, p)
        }
    }

    @Test fun onTouchMove() {
        val from = Random.nextInt()
        val absoluteFrom = Random.nextInt()
        val to = Random.nextInt()
        val absoluteTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val newList = mockk<MutableList<RollItem>>()

        spyViewModel.noteItem = noteItem

        FastMock.listExtension()

        every { spyViewModel.getAbsolutePosition(from) } returns absoluteFrom
        every { spyViewModel.getAbsolutePosition(to) } returns absoluteTo

        every { noteItem.list } returns list
        every { list.move(absoluteFrom, absoluteTo) } returns Unit

        every { spyViewModel.getList(noteItem) } returns newList

        assertTrue(spyViewModel.onTouchMove(from, to))

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onTouchMove(from, to)

            spyViewModel.noteItem
            spyViewModel.getAbsolutePosition(from)
            spyViewModel.noteItem
            spyViewModel.getAbsolutePosition(to)

            spyViewModel.noteItem
            noteItem.list
            list.move(absoluteFrom, absoluteTo)

            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.getList(noteItem)
            callback.notifyItemMoved(newList, from, to)

            spyViewModel.callback
            callback.hideKeyboard()
        }
    }

    @Test fun onTouchMoveResult() {
        val from = Random.nextInt()
        val absoluteFrom = Random.nextInt()
        val to = Random.nextInt()
        val absoluteTo = Random.nextInt()

        val noteItem = mockk<NoteItem.Roll>()
        val inputAccess = mockk<InputControl.Access>()

        spyViewModel.noteItem = noteItem

        every { spyViewModel.getAbsolutePosition(from) } returns absoluteFrom
        every { spyViewModel.getAbsolutePosition(to) } returns absoluteTo
        every { inputControl.access } returns inputAccess

        spyViewModel.onTouchMoveResult(from, to)

        verifyOrder {
            spyViewModel.getAbsolutePosition(from)
            spyViewModel.getAbsolutePosition(to)

            inputControl.onRollMove(absoluteFrom, absoluteTo)

            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
        }
    }


    @Test fun getList() {
        val item = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val hideList = mockk<MutableList<RollItem>>()

        FastMock.listExtension()

        every { item.isVisible } returns true
        every { item.list } returns list

        assertEquals(list, viewModel.getList(item))

        every { item.isVisible } returns false
        every { list.hide() } returns hideList

        assertEquals(hideList, viewModel.getList(item))

        verifySequence {
            item.list
            item.isVisible

            item.list
            item.isVisible
            list.hide()
        }
    }

    @Test fun notifyListByVisible() {
        TODO()
    }
}