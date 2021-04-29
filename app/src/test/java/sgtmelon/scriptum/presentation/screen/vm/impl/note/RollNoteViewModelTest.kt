package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.view.inputmethod.EditorInfo
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.*
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
//    @MockK lateinit var bindInteractor: IBindInteractor

    @MockK lateinit var saveControl: ISaveControl
    @MockK lateinit var inputControl: IInputControl

    private val viewModel by lazy { RollNoteViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel, recordPrivateCalls = true) }

    private val fastTest by lazy {
        FastTest.ViewModel(
            callback, parentCallback, interactor/*, bindInteractor*/,
            saveControl, inputControl, viewModel, spyViewModel, { FastMock.Note.deepCopy(it) },
            { verifyDeepCopy(it) }
        )
    }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor/*, bindInteractor*/)

        viewModel.inputControl = inputControl
        viewModel.saveControl = saveControl

        assertEquals(Note.Default.ID, viewModel.id)
        assertEquals(Note.Default.COLOR, viewModel.color)
        assertTrue(viewModel.mayAnimateIcon)
        assertTrue(viewModel.rankDialogItemArray.isEmpty())

        assertNotNull(viewModel.callback)
        assertNotNull(viewModel.parentCallback)
    }

    @After override fun tearDown() {
        super.tearDown()

        confirmVerified(
            callback, parentCallback, interactor/*, bindInteractor*/, inputControl, saveControl
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

        every { spyViewModel.getAdapterList() } returns rollList
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
            spyViewModel.getAdapterList()
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
        every { spyViewModel.getAdapterList() } returns rollList

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
            spyViewModel.getAdapterList()
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
            interactor.setVisible(noteItem)
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
        val list = mockk<MutableList<RollItem>>(relaxUnitFun = true)
        val adapterList = mockk<MutableList<RollItem>>()
        val size = Random.nextInt()
        val access = mockk<InputControl.Access>()

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        every { callback.getEnterText() } returns enterText
        every { noteItem.list } returns list
        every { list.size } returns size
        every { inputControl.access } returns access
        every { spyViewModel.getAdapterList() } returns adapterList

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
            list.add(0, firstRollItem)
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)
            spyViewModel.getAdapterList()
            callback.scrollToItem(simpleClick = false, p = 0, list = adapterList)


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
            list.size
            spyViewModel.inputControl
            inputControl.onRollAdd(p = size, valueTo = secondRollItem.toJson())
            spyViewModel.noteItem
            noteItem.list
            list.add(size, secondRollItem)
            spyViewModel.callback
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)
            spyViewModel.getAdapterList()
            callback.scrollToItem(simpleClick = true, p = size, list = adapterList)
        }
    }

    @Test fun onClickItemCheck() {
        val p = Random.nextInt()
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)

        val absolutePosition = Random.nextInt()
        val adapterList = mockk<MutableList<RollItem>>()
        val list = mockk<MutableList<RollItem>>()
        val check = Random.nextInt()
        val size = Random.nextInt()

        every { spyViewModel.getAdapterList() } returns adapterList
        every { spyViewModel.cacheData() } returns Unit
        every { noteItem.getCheck() } returns check
        every { noteItem.list } returns list
        every { list.size } returns size

        spyViewModel.noteState = noteState
        spyViewModel.noteItem = noteItem

        every { noteState.isEdit } returns true
        spyViewModel.onClickItemCheck(p)

        every { noteState.isEdit } returns false
        every { spyViewModel.getAbsolutePosition(p) } returns null
        spyViewModel.onClickItemCheck(p)

        every { spyViewModel.getAbsolutePosition(p) } returns absolutePosition
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
            spyViewModel.getAbsolutePosition(p)

            spyViewModel.onClickItemCheck(p)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.getAbsolutePosition(p)
            spyViewModel.noteItem
            noteItem.onItemCheck(absolutePosition)
            spyViewModel.cacheData()
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.notifyItemRemoved(adapterList, p)
            spyViewModel.noteItem
            spyViewModel.callback
            noteItem.getCheck()
            noteItem.list
            list.size
            callback.updateProgress(check, size)
            spyViewModel.noteItem
            interactor.updateRollCheck(noteItem, absolutePosition)

            spyViewModel.onClickItemCheck(p)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.getAbsolutePosition(p)
            spyViewModel.noteItem
            noteItem.onItemCheck(absolutePosition)
            spyViewModel.cacheData()
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.notifyItemChanged(adapterList, p)
            spyViewModel.noteItem
            spyViewModel.callback
            noteItem.getCheck()
            noteItem.list
            list.size
            callback.updateProgress(check, size)
            spyViewModel.noteItem
            interactor.updateRollCheck(noteItem, absolutePosition)
        }
    }

    @Test fun onLongClickItemCheck() {
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val noteItem = mockk<NoteItem.Roll>(relaxUnitFun = true)

        val isCheck = Random.nextBoolean()
        val adapterList = mockk<MutableList<RollItem>>()
        val list = mockk<MutableList<RollItem>>()
        val check = Random.nextInt()
        val size = Random.nextInt()

        every { noteItem.onItemLongCheck() } returns isCheck
        every { spyViewModel.cacheData() } returns Unit
        every { spyViewModel.getAdapterList() } returns adapterList
        every { noteItem.getCheck() } returns check
        every { noteItem.list } returns list
        every { list.size } returns size
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
            spyViewModel.getAdapterList()
            callback.notifyDataRangeChanged(adapterList)
            callback.changeCheckToggle(state = false)
            spyViewModel.noteItem
            noteItem.getCheck()
            noteItem.list
            list.size
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
            spyViewModel.getAdapterList()
            callback.notifyDataRangeChanged(adapterList)
            callback.changeCheckToggle(state = false)
            spyViewModel.noteItem
            noteItem.getCheck()
            noteItem.list
            list.size
            callback.updateProgress(check, size)

            spyViewModel.interactor
            spyViewModel.noteItem
            interactor.updateRollCheck(noteItem, isCheck)
        }
    }

    //region Dialog results

    @Test fun onResultColorDialog() = fastTest.onResultColorDialog(mockk())

    @Test fun onResultRankDialog() = fastTest.onResultRankDialog(mockk())

    @Test fun onResultDateDialog() = fastTest.onResultDateDialog()

    @Test fun onResultDateDialogClear() = fastTest.onResultDateDialogClear(mockk(), mockk())

    @Test fun onResultTimeDialog() = fastTest.onResultTimeDialog(mockk(), mockk())

    @Test fun onResultConvertDialog() = fastTest.onResultConvertDialog(mockk())

    //endregion

    @Test fun onReceiveUnbindNote() = fastTest.onReceiveUnbindNote(mockk(), mockk())

    //region Menu click

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

        every { item.tag } returns InputAction.ROLL
        every { spyViewModel.onMenuUndoRedoRoll(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.ROLL_ADD
        every { spyViewModel.onMenuUndoRedoAdd(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

        every { item.tag } returns InputAction.ROLL_REMOVE
        every { spyViewModel.onMenuUndoRedoRemove(item, isUndo) } returns Unit
        spyViewModel.onMenuUndoRedoSelect(item, isUndo)

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

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoRoll(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoAdd(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true

            spyViewModel.onMenuUndoRedoSelect(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = false
            item.tag
            spyViewModel.onMenuUndoRedoRemove(item, isUndo)
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
        every { item[isUndo] } returns text
        every { rollItem.text = text } returns Unit
        every { spyViewModel.getAdapterList() } returns returnList
        every { returnList.validIndexOf(rollItem) } returns null

        spyViewModel.onMenuUndoRedoRoll(item, isUndo)

        every { returnList.validIndexOf(rollItem) } returns validIndex
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
            item[isUndo]
            rollItem.text = text
            spyViewModel.getAdapterList()
            returnList.validIndexOf(rollItem)

            spyViewModel.onMenuUndoRedoRoll(item, isUndo)

            spyViewModel.noteItem
            noteItem.list
            item.p
            item[isUndo]
            rollItem.text = text
            spyViewModel.getAdapterList()
            returnList.validIndexOf(rollItem)
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
            item[isUndo]
            rollItem.text = text
            spyViewModel.getAdapterList()
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.noteItem
            noteItem.isVisible
            rollItem.isCheck

            spyViewModel.onMenuUndoRedoRoll(item, isUndo)

            spyViewModel.noteItem
            noteItem.list
            item.p
            item[isUndo]
            rollItem.text = text
            spyViewModel.getAdapterList()
            returnList.validIndexOf(rollItem)
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

        every { spyViewModel.onRemoveItem(item) } returns Unit
        spyViewModel.onMenuUndoRedoAdd(item, isUndo = true)

        every { spyViewModel.onInsertItem(item, isUndo = false) } returns Unit
        spyViewModel.onMenuUndoRedoAdd(item, isUndo = false)

        verifySequence {
            spyViewModel.onMenuUndoRedoAdd(item, isUndo = true)
            spyViewModel.onRemoveItem(item)

            spyViewModel.onMenuUndoRedoAdd(item, isUndo = false)
            spyViewModel.onInsertItem(item, isUndo = false)
        }
    }

    @Test fun onMenuUndoRedoRemove() {
        val item = mockk<InputItem>()

        every { spyViewModel.onInsertItem(item, isUndo = true) } returns Unit
        spyViewModel.onMenuUndoRedoRemove(item, isUndo = true)

        every { spyViewModel.onRemoveItem(item) } returns Unit
        spyViewModel.onMenuUndoRedoRemove(item, isUndo = false)

        verifySequence {
            spyViewModel.onMenuUndoRedoRemove(item, isUndo = true)
            spyViewModel.onInsertItem(item, isUndo = true)

            spyViewModel.onMenuUndoRedoRemove(item, isUndo = false)
            spyViewModel.onRemoveItem(item)
        }
    }

    @Test fun onRemoveItem() {
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
        spyViewModel.onRemoveItem(item)

        every { item.p } returns p
        every { spyViewModel.getAdapterList() } returns returnList
        every { returnList.validIndexOf(rollItem) } returns null
        every { list.validRemoveAt(p) } returns null

        spyViewModel.onRemoveItem(item)

        every { list.validRemoveAt(p) } returns rollItem

        spyViewModel.onRemoveItem(item)

        every { returnList.validIndexOf(rollItem) } returns validIndex
        every { noteItem.isVisible } returns true

        spyViewModel.onRemoveItem(item)

        every { noteItem.isVisible } returns false
        every { rollItem.isCheck } returns true

        spyViewModel.onRemoveItem(item)

        every { rollItem.isCheck } returns false

        spyViewModel.onRemoveItem(item)

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onRemoveItem(item)

            spyViewModel.noteItem
            noteItem.list
            item.p

            spyViewModel.onRemoveItem(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.getAdapterList()
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.validRemoveAt(p)

            spyViewModel.onRemoveItem(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.getAdapterList()
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.validRemoveAt(p)

            spyViewModel.onRemoveItem(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.getAdapterList()
            returnList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.validRemoveAt(p)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.notifyItemRemoved(returnList, validIndex)

            spyViewModel.onRemoveItem(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.getAdapterList()
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

            spyViewModel.onRemoveItem(item)

            spyViewModel.noteItem
            noteItem.list
            item.p
            spyViewModel.getAdapterList()
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
            spyViewModel.getAdapterList()
            callback.notifyItemRemoved(returnList, validIndex)
        }
    }

    @Test fun onInsertItem() {
        val item = mockk<InputItem>()
        val isUndo = Random.nextBoolean()
        val data = nextString()
        val rollItem = mockk<RollItem>()
        val noteItem = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val p = Random.nextInt()
        val adapterList = mockk<MutableList<RollItem>>()

        val position = Random.nextInt()
        val text = nextString()

        every { item[isUndo] } returns data
        mockkObject(RollItem)
        every { RollItem[data] } returns null
        viewModel.onInsertItem(item, isUndo)

        /**
         * It should be before [spyViewModel] first call, cause of lazy initialization.
         */
        viewModel.noteItem = noteItem

        every { RollItem[data] } returns rollItem
        every { noteItem.list } returns list
        every { item.p } returns p
        every { list.add(p, rollItem) } returns Unit
        every { spyViewModel.getAdapterList() } returns adapterList
        every { spyViewModel.getInsertPosition(item, rollItem) } returns null
        spyViewModel.onInsertItem(item, isUndo)

        every { spyViewModel.getInsertPosition(item, rollItem) } returns position
        every { rollItem.text } returns text
        spyViewModel.onInsertItem(item, isUndo)

        verifySequence {
            item[isUndo]
            RollItem[data]

            spyViewModel.onInsertItem(item, isUndo)
            item[isUndo]
            RollItem[data]
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.add(p, rollItem)
            spyViewModel.getAdapterList()
            spyViewModel.getInsertPosition(item, rollItem)

            spyViewModel.onInsertItem(item, isUndo)
            item[isUndo]
            RollItem[data]
            spyViewModel.noteItem
            noteItem.list
            item.p
            list.add(p, rollItem)
            spyViewModel.getAdapterList()
            spyViewModel.getInsertPosition(item, rollItem)
            rollItem.text
            spyViewModel.callback
            callback.notifyItemInserted(adapterList, position, text.length)
        }
    }

    @Test fun getInsertPosition() {
        val noteItem = mockk<NoteItem.Roll>()
        val size = 3 * getRandomSize()
        val list = MutableList<RollItem>(size) { mockk() }
        val hideSize = getRandomSize()
        val hideList = mockk<MutableList<RollItem>>()

        val item = mockk<InputItem>()
        val p = list.indices.random()
        val rollItem = mockk<RollItem>()
        val sublist = list.subList(0, p)

        every { noteItem.isVisible } returns false
        every { rollItem.isCheck } returns true
        viewModel.noteItem = noteItem
        assertNull(viewModel.getInsertPosition(item, rollItem))

        every { noteItem.isVisible } returns true
        every { item.p } returns p
        assertEquals(p, viewModel.getInsertPosition(item, rollItem))

        every { noteItem.isVisible } returns false
        every { rollItem.isCheck } returns false
        FastMock.listExtension()
        every { noteItem.list } returns list
        every { sublist.hide() } returns hideList
        every { hideList.size } returns hideSize
        assertEquals(hideSize, viewModel.getInsertPosition(item, rollItem))

        verifySequence {
            noteItem.isVisible
            rollItem.isCheck

            noteItem.isVisible
            item.p

            noteItem.isVisible
            rollItem.isCheck
            noteItem.list
            item.p
            sublist.hide()
            hideList.size
        }
    }

    @Test fun onMenuUndoRedoMove() {
        val item = mockk<InputItem>()
        val isUndo = Random.nextBoolean()

        val noteItem = mockk<NoteItem.Roll>()
        val size = getRandomSize()
        val list = MutableList<RollItem>(size) { mockk() }
        val from = list.indices.random()
        val to = Random.nextInt()

        val adapterList = mockk<MutableList<RollItem>>()
        val rollItem = list[from]
        val validIndex = Random.nextInt()

        FastMock.listExtension()

        every { item[!isUndo] } returns nextString()
        viewModel.onMenuUndoRedoMove(item, isUndo)

        /**
         * Return not index of list for getOnNull return call.
         */
        every { item[!isUndo] } returns "-1"
        every { item[isUndo] } returns nextString()
        viewModel.onMenuUndoRedoMove(item, isUndo)

        every { item[isUndo] } returns to.toString()
        every { noteItem.list } returns list
        viewModel.noteItem = noteItem
        viewModel.onMenuUndoRedoMove(item, isUndo)

        every { item[!isUndo] } returns from.toString()
        every { spyViewModel.getAdapterList() } returns adapterList
        every { adapterList.validIndexOf(rollItem) } returns null
        every { list.move(from, to) } returns Unit
        spyViewModel.onMenuUndoRedoMove(item, isUndo)

        every { adapterList.validIndexOf(rollItem) } returns validIndex
        every { noteItem.isVisible } returns true
        spyViewModel.onMenuUndoRedoMove(item, isUndo)

        every { noteItem.isVisible } returns false
        every { rollItem.isCheck } returns true

        spyViewModel.onMenuUndoRedoMove(item, isUndo)

        every { rollItem.isCheck } returns false

        spyViewModel.onMenuUndoRedoMove(item, isUndo)

        verifySequence {
            item[!isUndo]

            item[!isUndo]
            item[isUndo]

            item[!isUndo]
            item[isUndo]
            noteItem.list

            spyViewModel.onMenuUndoRedoMove(item, isUndo)
            item[!isUndo]
            item[isUndo]
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            list.move(from, to)
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)

            spyViewModel.onMenuUndoRedoMove(item, isUndo)
            item[!isUndo]
            item[isUndo]
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            list.move(from, to)
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.notifyItemMoved(adapterList, validIndex, validIndex)

            spyViewModel.onMenuUndoRedoMove(item, isUndo)
            item[!isUndo]
            item[isUndo]
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            list.move(from, to)
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.noteItem
            noteItem.isVisible
            rollItem.isCheck

            spyViewModel.onMenuUndoRedoMove(item, isUndo)
            item[!isUndo]
            item[isUndo]
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.list
            list.move(from, to)
            spyViewModel.getAdapterList()
            adapterList.validIndexOf(rollItem)
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.noteItem
            noteItem.isVisible
            rollItem.isCheck
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.notifyItemMoved(adapterList, validIndex, validIndex)
        }
    }

    @Test fun onMenuRank() = fastTest.onMenuRank(mockk())

    @Test fun onMenuColor() = fastTest.onMenuColor(mockk())

    @Test fun onMenuSave_startSkip() {
        val noteItem = mockk<NoteItem.Roll>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)

        viewModel.noteState = noteState
        viewModel.noteItem = noteItem

        every { callback.isDialogOpen } returns true
        assertFalse(viewModel.onMenuSave(changeMode = true))

        every { noteState.isEdit } returns false
        every { callback.isDialogOpen } returns false
        assertFalse(viewModel.onMenuSave(changeMode = true))

        assertFalse(viewModel.onMenuSave(changeMode = false))

        every { noteState.isEdit } returns true
        every { noteItem.isSaveEnabled() } returns false
        assertFalse(viewModel.onMenuSave(Random.nextBoolean()))

        coVerify {
            callback.isDialogOpen

            callback.isDialogOpen
            noteState.isEdit

            noteState.isEdit

            noteState.isEdit
            noteItem.isSaveEnabled()
        }
    }

    @Test fun onMenuSave_notChangeMode() {
        val noteItem = mockk<NoteItem.Roll>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val color = Random.nextInt()
        val list = mockk<MutableList<RollItem>>()

        viewModel.noteState = noteState
        viewModel.noteItem = noteItem

        every { noteState.isEdit } returns true
        every { noteItem.isSaveEnabled() } returns true
        every { noteItem.onSave() } returns Unit
        every { spyViewModel.getAdapterList() } returns list
        every { noteState.isCreate } returns false
        every { noteItem.color } returns color
        coEvery { spyViewModel.saveBackgroundWork() } returns Unit
        assertTrue(spyViewModel.onMenuSave(changeMode = false))

        every { noteState.isCreate } returns true
        assertTrue(spyViewModel.onMenuSave(changeMode = false))

        coVerify {
            spyViewModel.onMenuSave(changeMode = false)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.isSaveEnabled()
            spyViewModel.noteItem
            noteItem.onSave()
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.setList(list)
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.noteItem
            noteItem.color
            spyViewModel.parentCallback
            spyViewModel.noteItem
            noteItem.color
            parentCallback.onUpdateNoteColor(color)
            spyViewModel.saveBackgroundWork()

            spyViewModel.onMenuSave(changeMode = false)
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.isSaveEnabled()
            spyViewModel.noteItem
            noteItem.onSave()
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.setList(list)
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.callback
            callback.setToolbarBackIcon(isCancel = true, needAnim = true)
            spyViewModel.noteItem
            noteItem.color
            spyViewModel.parentCallback
            spyViewModel.noteItem
            noteItem.color
            parentCallback.onUpdateNoteColor(color)
            spyViewModel.saveBackgroundWork()
        }
    }

    @Test fun onMenuSave_changeMode() {
        val noteItem = mockk<NoteItem.Roll>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val color = Random.nextInt()
        val list = mockk<MutableList<RollItem>>()

        viewModel.noteState = noteState
        viewModel.noteItem = noteItem

        every { callback.isDialogOpen } returns false
        every { noteState.isEdit } returns true
        every { noteItem.isSaveEnabled() } returns true
        every { noteItem.onSave() } returns Unit
        every { spyViewModel.getAdapterList() } returns list
        every { spyViewModel.setupEditMode(isEdit = false) } returns Unit
        every { noteItem.color } returns color
        coEvery { spyViewModel.saveBackgroundWork() } returns Unit
        assertTrue(spyViewModel.onMenuSave(changeMode = true))

        coVerify {
            spyViewModel.onMenuSave(changeMode = true)
            spyViewModel.callback
            callback.isDialogOpen
            spyViewModel.noteState
            noteState.isEdit
            spyViewModel.noteItem
            noteItem.isSaveEnabled()
            spyViewModel.noteItem
            noteItem.onSave()
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.setList(list)
            spyViewModel.callback
            callback.hideKeyboard()
            spyViewModel.setupEditMode(isEdit = false)
            spyViewModel.inputControl
            inputControl.reset()
            spyViewModel.noteItem
            noteItem.color
            spyViewModel.parentCallback
            spyViewModel.noteItem
            noteItem.color
            parentCallback.onUpdateNoteColor(color)
            spyViewModel.saveBackgroundWork()
        }
    }

    @Test fun saveBackgroundWork() = startCoTest {
        val noteItem = mockk<NoteItem.Roll>()
        val noteState = mockk<NoteState>()
        val id = Random.nextLong()
        val adapterList = mockk<MutableList<RollItem>>()

        every { spyViewModel.cacheData() } returns Unit
        every { noteState.isCreate } returns false
        every { spyViewModel.getAdapterList() } returns adapterList

        spyViewModel.noteItem = noteItem
        spyViewModel.noteState = noteState
        spyViewModel.saveBackgroundWork()
        assertEquals(Note.Default.ID, spyViewModel.id)

        every { noteState.isCreate } returns true
        every { noteState.isCreate = NoteState.ND_CREATE } returns Unit
        every { noteItem.id } returns id

        spyViewModel.saveBackgroundWork()
        assertEquals(id, spyViewModel.id)

        coVerifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.noteState = noteState
            spyViewModel.saveBackgroundWork()
            spyViewModel.interactor
            spyViewModel.noteItem
            spyViewModel.noteState
            noteState.isCreate
            interactor.saveNote(noteItem, isCreate = false)
            spyViewModel.cacheData()
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.setList(adapterList)
            spyViewModel.id

            spyViewModel.saveBackgroundWork()
            spyViewModel.interactor
            spyViewModel.noteItem
            spyViewModel.noteState
            noteState.isCreate
            interactor.saveNote(noteItem, isCreate = true)
            spyViewModel.cacheData()
            spyViewModel.noteState
            noteState.isCreate
            spyViewModel.noteState
            noteState.isCreate = NoteState.ND_CREATE
            spyViewModel.noteItem
            noteItem.id
            spyViewModel.id = id
            spyViewModel.parentCallback
            spyViewModel.id
            parentCallback.onUpdateNoteId(id)
            spyViewModel.interactor
            spyViewModel.noteItem
            interactor.setVisible(noteItem)
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.setList(adapterList)
            spyViewModel.id
        }
    }

    @Test fun onMenuNotification() = fastTest.onMenuNotification(mockk())

    @Test fun onMenuBind() = fastTest.onMenuBind(mockk(), mockk())

    @Test fun onMenuConvert() = fastTest.onMenuConvert()

    @Test fun onMenuDelete() = fastTest.onMenuDelete(mockk())

    @Test fun onMenuEdit() = fastTest.onMenuEdit()

    @Test fun setupEditMode() {
        val noteItem = mockk<NoteItem.Roll>()
        val noteState = mockk<NoteState>(relaxUnitFun = true)

        val isCreate = Random.nextBoolean()
        val mayAnimateIcon = Random.nextBoolean()
        val access = mockk<InputControl.Access>()
        val check = Random.nextInt()
        val list = mockk<MutableList<RollItem>>()
        val size = Random.nextInt()

        every { noteState.isCreate } returns isCreate
        every { inputControl.access } returns access
        every { noteItem.getCheck() } returns check
        every { noteItem.list } returns list
        every { list.size } returns size

        viewModel.noteItem = noteItem
        viewModel.noteState = noteState
        viewModel.mayAnimateIcon = mayAnimateIcon

        viewModel.setupEditMode(isEdit = false)
        viewModel.setupEditMode(isEdit = true)

        verifySequence {
            inputControl.isEnabled = false
            noteState.isEdit = false
            noteState.isCreate
            callback.setToolbarBackIcon(
                isCancel = false,
                needAnim = !isCreate && mayAnimateIcon
            )
            callback.onBindingEdit(noteItem, isEditMode = false)
            inputControl.access
            callback.onBindingInput(noteItem, access)
            callback.updateNoteState(noteState)
            noteItem.getCheck()
            noteItem.list
            list.size
            callback.updateProgress(check, size)
            saveControl.needSave = true
            saveControl.setSaveEvent(isWork = false)
            inputControl.isEnabled = true

            inputControl.isEnabled = false
            noteState.isEdit = true
            noteState.isCreate
            noteState.isCreate
            callback.setToolbarBackIcon(
                isCancel = !isCreate,
                needAnim = !isCreate && mayAnimateIcon
            )
            callback.onBindingEdit(noteItem, isEditMode = true)
            inputControl.access
            callback.onBindingInput(noteItem, access)
            callback.updateNoteState(noteState)
            noteState.isCreate
            callback.focusOnEdit(isCreate)
            saveControl.needSave = true
            saveControl.setSaveEvent(isWork = true)
            inputControl.isEnabled = true
        }
    }

    //endregion

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

        every { spyViewModel.getAbsolutePosition(p) } returns null

        spyViewModel.noteItem = noteItem
        spyViewModel.onInputRollChange(p, text)

        every { spyViewModel.getAbsolutePosition(p) } returns -1
        every { noteItem.list } returns list
        every { spyViewModel.getAdapterList() } returns newList
        every { inputControl.access } returns access

        spyViewModel.onInputRollChange(p, text)

        every { spyViewModel.getAbsolutePosition(p) } returns absolutePosition
        every { item.text = text } returns Unit

        spyViewModel.onInputRollChange(p, text)

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onInputRollChange(p, text)
            spyViewModel.getAbsolutePosition(p)

            spyViewModel.onInputRollChange(p, text)
            spyViewModel.getAbsolutePosition(p)
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.setList(newList)
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)

            spyViewModel.onInputRollChange(p, text)
            spyViewModel.getAbsolutePosition(p)
            spyViewModel.noteItem
            noteItem.list
            item.text = text
            spyViewModel.callback
            spyViewModel.getAdapterList()
            callback.setList(newList)
            spyViewModel.noteItem
            spyViewModel.inputControl
            inputControl.access
            callback.onBindingInput(noteItem, access)
        }
    }

    @Test fun getAbsolutePosition() {
        val item = mockk<NoteItem.Roll>()
        val adapterPosition = Random.nextInt()
        val list = MutableList(getRandomSize()) { mockk<RollItem>() }
        val hideList = MutableList(getRandomSize()) { mockk<RollItem>() }
        val hideListPosition = hideList.indices.random()
        val hideItem = hideList[hideListPosition]
        val absolutePosition = Random.nextInt()

        FastMock.listExtension()

        every { item.isVisible } returns true
        viewModel.noteItem = item
        assertEquals(adapterPosition, viewModel.getAbsolutePosition(adapterPosition))

        every { item.isVisible } returns false
        every { item.list } returns list
        every { list.hide() } returns hideList
        assertNull(viewModel.getAbsolutePosition(adapterPosition = -1))

        every { list.validIndexOf(hideItem) } returns null
        assertNull(viewModel.getAbsolutePosition(hideListPosition))

        every { list.validIndexOf(hideItem) } returns absolutePosition
        assertEquals(absolutePosition, viewModel.getAbsolutePosition(hideListPosition))

        verifySequence {
            item.isVisible

            item.isVisible
            item.list
            list.hide()

            item.isVisible
            item.list
            list.hide()
            list.validIndexOf(hideItem)

            item.isVisible
            item.list
            list.hide()
            list.validIndexOf(hideItem)
        }
    }

    @Test fun onRollActionNext() {
        viewModel.onRollActionNext()

        verifySequence {
            callback.onFocusEnter()
        }
    }

    //region Touch callbacks

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

        FastMock.listExtension()

        every { spyViewModel.getAbsolutePosition(p) } returns null

        spyViewModel.onTouchSwiped(p)

        every { spyViewModel.getAbsolutePosition(p) } returns absolutePosition
        every { noteItem.list } returns list
        every { list.validRemoveAt(absolutePosition) } returns null

        spyViewModel.noteItem = noteItem
        spyViewModel.onTouchSwiped(p)

        every { list.validRemoveAt(absolutePosition) } returns item
        every { item.toJson() } returns itemJson
        every { inputControl.access } returns inputAccess
        every { spyViewModel.getAdapterList() } returns newList

        spyViewModel.onTouchSwiped(p)

        verifyOrder {
            spyViewModel.onTouchSwiped(p)
            spyViewModel.getAbsolutePosition(p)

            spyViewModel.noteItem = noteItem
            spyViewModel.onTouchSwiped(p)
            spyViewModel.getAbsolutePosition(p)
            noteItem.list
            list.validRemoveAt(absolutePosition)

            spyViewModel.onTouchSwiped(p)
            spyViewModel.getAbsolutePosition(p)
            noteItem.list
            list.validRemoveAt(absolutePosition)
            item.toJson()
            inputControl.onRollRemove(absolutePosition, itemJson)
            inputControl.access
            callback.onBindingInput(noteItem, inputAccess)
            spyViewModel.getAdapterList()
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

        every { spyViewModel.getAbsolutePosition(from) } returns null

        assertTrue(spyViewModel.onTouchMove(from, to))

        every { spyViewModel.getAbsolutePosition(from) } returns absoluteFrom
        every { spyViewModel.getAbsolutePosition(to) } returns null

        assertTrue(spyViewModel.onTouchMove(from, to))

        every { spyViewModel.getAbsolutePosition(to) } returns absoluteTo
        every { noteItem.list } returns list
        every { list.move(absoluteFrom, absoluteTo) } returns Unit
        every { spyViewModel.getAdapterList() } returns newList

        assertTrue(spyViewModel.onTouchMove(from, to))

        verifySequence {
            spyViewModel.noteItem = noteItem
            spyViewModel.onTouchMove(from, to)
            spyViewModel.getAbsolutePosition(from)

            spyViewModel.onTouchMove(from, to)
            spyViewModel.getAbsolutePosition(from)
            spyViewModel.getAbsolutePosition(to)

            spyViewModel.onTouchMove(from, to)
            spyViewModel.getAbsolutePosition(from)
            spyViewModel.getAbsolutePosition(to)
            spyViewModel.noteItem
            noteItem.list
            list.move(absoluteFrom, absoluteTo)
            spyViewModel.callback
            spyViewModel.getAdapterList()
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

    //endregion

    @Test fun getAdapterList() {
        val item = mockk<NoteItem.Roll>()
        val list = mockk<MutableList<RollItem>>()
        val hideList = mockk<MutableList<RollItem>>()

        FastMock.listExtension()

        every { item.isVisible } returns true
        every { item.list } returns list

        viewModel.noteItem = item

        assertEquals(list, viewModel.getAdapterList())

        every { item.isVisible } returns false
        every { list.hide() } returns hideList

        assertEquals(hideList, viewModel.getAdapterList())

        verifySequence {
            item.list
            item.isVisible

            item.list
            item.isVisible
            list.hide()
        }
    }

    @Test fun notifyListByVisible() {
        val noteItem = mockk<NoteItem.Roll>()
        val list = MutableList<RollItem>(getRandomSize()) { mockk() }

        every { noteItem.list } returns mutableListOf()
        viewModel.noteItem = noteItem
        viewModel.notifyListByVisible()

        every { noteItem.list } returns list
        every { noteItem.isVisible } returns false
        every { spyViewModel.notifyInvisibleList(list) } returns Unit
        spyViewModel.notifyListByVisible()

        every { noteItem.isVisible } returns true
        every { spyViewModel.notifyVisibleList(list) } returns Unit
        spyViewModel.notifyListByVisible()

        verifySequence {
            noteItem.list

            spyViewModel.notifyListByVisible()
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.notifyInvisibleList(list)

            spyViewModel.notifyListByVisible()
            spyViewModel.noteItem
            noteItem.list
            spyViewModel.noteItem
            noteItem.isVisible
            spyViewModel.notifyVisibleList(list)
        }
    }

    @Test fun notifyVisibleList_allCheck() {
        val size = getRandomSize()
        val list = MutableList(size) {
            RollItem(Random.nextLong(), Random.nextInt(), isCheck = true, nextString())
        }

        viewModel.notifyVisibleList(list)

        verifySequence {
            callback.animateInfoVisible(isVisible = false)

            for (i in list.indices) {
                callback.notifyItemInserted(list, i)
            }
        }
    }

    @Test fun notifyVisibleList_notAllCheck() {
        val size = getRandomSize()
        val list = MutableList(size) {
            RollItem(Random.nextLong(), Random.nextInt(), Random.nextBoolean(), nextString())
        }

        /**
         * Need be sure for 100%.
         */
        repeat(times = size / 3) {
            list.random().isCheck = false
        }

        val filterList = list.filter { it.isCheck }

        viewModel.notifyVisibleList(list)

        verifySequence {
            for (item in filterList) {
                val index = list.validIndexOf(item) ?: continue
                callback.notifyItemInserted(list, index)
            }
        }
    }

    @Test fun notifyInvisibleList() {
        val size = getRandomSize()
        val list = List(size) {
            RollItem(Random.nextLong(), Random.nextInt(), Random.nextBoolean(), nextString())
        }
        val resultList = list.filter { !it.isCheck }
        val checkList = ArrayList(list).toMutableList()
        val verifyList = ArrayList(list).toMutableList()

        viewModel.notifyInvisibleList(checkList)

        assertEquals(resultList, checkList)

        verifySequence {
            for (item in list.filter { it.isCheck }) {
                val index = verifyList.indexOf(item)
                verifyList.validRemoveAt(index)
                callback.notifyItemRemoved(resultList, index)
            }
        }
    }
}