package sgtmelon.scriptum.presentation.screen.vm.impl.note

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.ITextNoteFragment
import kotlin.random.Random

/**
 * Test for [TextNoteViewModel].
 */
@ExperimentalCoroutinesApi
class TextNoteViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: ITextNoteFragment
    @MockK lateinit var parentCallback: INoteConnector

    @MockK lateinit var interactor: ITextNoteInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    @MockK lateinit var inputControl: IInputControl
    @MockK lateinit var saveControl: ISaveControl

    private val viewModel by lazy { TextNoteViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel, recordPrivateCalls = true) }

    private val fastTest by lazy {
        FastTest.ViewModel(
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

        assertEquals(Default.ID, viewModel.id)
        assertEquals(Default.COLOR, viewModel.color)
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
        val theme = Random.nextInt()
        val color = Random.nextInt()

        viewModel.color = color
        every { interactor.theme } returns theme

        viewModel.setupBeforeInitialize()

        verifySequence {
            interactor.theme
            callback.apply {
                setupBinding(theme)
                setupToolbar(theme, color)
                setupEnter(inputControl)
            }
        }
    }

    @Test fun tryInitializeNote() = startCoTest {
        val name = nextString()
        val itemArray = Array(size = 10) { nextString() }
        val defaultColor = Random.nextInt()
        val noteItem = mockk<NoteItem.Text>()
        val id = Random.nextLong()
        val isBin = Random.nextBoolean()

        every { spyViewModel.isNoteInitialized() } returns true

        assertTrue(spyViewModel.tryInitializeNote())

        every { spyViewModel.isNoteInitialized() } returns false
        every { parentCallback.getString(R.string.dialog_item_rank) } returns name
        coEvery { interactor.getRankDialogItemArray(name) } returns itemArray
        every { interactor.defaultColor } returns defaultColor
        mockkObject(NoteItem.Text)
        every { NoteItem.Text.getCreate(defaultColor) } returns noteItem
        every { spyViewModel.cacheData() } returns Unit

        assertTrue(spyViewModel.tryInitializeNote())

        coEvery { interactor.getItem(id) } returns null

        spyViewModel.id = id
        assertFalse(spyViewModel.tryInitializeNote())

        coEvery { interactor.getItem(id) } returns noteItem
        mockDeepCopy(noteItem)
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
            NoteItem.Text.getCreate(defaultColor)
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
        val noteState = mockk<NoteState>(relaxUnitFun = true)
        val isRankEmpty = Random.nextBoolean()
        val rankDialogItemArray = if (isRankEmpty) {
            arrayOf(nextString())
        } else {
            arrayOf(nextString(), nextString())
        }
        val isEdit = Random.nextBoolean()

        every { spyViewModel.setupEditMode(isEdit) } returns Unit
        every { noteState.isEdit } returns isEdit

        spyViewModel.rankDialogItemArray = rankDialogItemArray
        spyViewModel.noteState = noteState
        spyViewModel.setupAfterInitialize()

        coVerify {
            spyViewModel.rankDialogItemArray = rankDialogItemArray
            spyViewModel.noteState
            spyViewModel.setupAfterInitialize()

            spyViewModel.callback
            callback.setupDialog(rankDialogItemArray)

            spyViewModel.mayAnimateIcon = false
            noteState.isEdit
            spyViewModel.setupEditMode(isEdit)
            spyViewModel.mayAnimateIcon = true

            spyViewModel.callback
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

        val noteItem = mockk<NoteItem.Text>(relaxUnitFun = true)
        val restoreItem = mockk<NoteItem.Text>(relaxUnitFun = true)

        val id = Random.nextLong()
        val colorFrom = Random.nextInt()
        val colorTo = Random.nextInt()

        every { noteItem.color } returns colorFrom
        every { spyViewModel.setupEditMode(isEdit = false) } returns Unit

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

            spyViewModel.setupEditMode(isEdit = false)
            spyViewModel.callback
            callback.tintToolbar(colorFrom, colorTo)
            spyViewModel.parentCallback
            parentCallback.onUpdateNoteColor(colorTo)
            spyViewModel.inputControl
            inputControl.reset()
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

        every { item.tag } returns InputAction.TEXT
        every { spyViewModel.onMenuUndoRedoText(item, isUndo) } returns Unit
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
            spyViewModel.onMenuUndoRedoText(item, isUndo)
            spyViewModel.inputControl
            inputControl.isEnabled = true
        }
    }

    @Test fun onMenuUndoRedoRank() = fastTest.onMenuUndoRedoRank(mockk(relaxUnitFun = true))

    @Test fun onMenuUndoRedoColor() = fastTest.onMenuUndoRedoColor(mockk())

    @Test fun onMenuUndoRedoName() = fastTest.onMenuUndoRedoName()

    @Test fun onMenuUndoRedoText() {
        val item = mockk<InputItem>()
        val isUndo = Random.nextBoolean()
        val cursor = mockk<InputItem.Cursor>(relaxUnitFun = true)

        val text = nextString()
        val position = Random.nextInt()

        mockkObject(InputItem.Cursor)

        every { item[isUndo] } returns text
        every { item.cursor } returns cursor
        every { cursor[isUndo] } returns position

        viewModel.onMenuUndoRedoText(item, isUndo)

        verifySequence {
            item[isUndo]
            item.cursor
            cursor[isUndo]
            callback.changeText(text, position)
        }
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
//        val noteItem = mockk<NoteItem.Text>()
//        val noteState = mockk<NoteState>(relaxUnitFun = true)
//        val iconState = mockk<IconState>(relaxUnitFun = true)
//
//        val isCreate = Random.nextBoolean()
//        val mayAnimate = Random.nextBoolean()
//
//        every { noteState.isCreate } returns isCreate
//        every { iconState.mayAnimate } returns mayAnimate
//
//        viewModel.noteItem = noteItem
//        viewModel.noteState = noteState
//        viewModel.iconState = iconState
//
//        viewModel.setupEditMode(isEdit = false)
//        viewModel.setupEditMode(isEdit = true)
//
//        verifySequence {
//            inputControl.isEnabled = false
//            noteState.isEdit = false
//            callback.setToolbarBackIcon(
//                isCancel =
//            )
//            inputControl.isEnabled = true
        //        }
    }


    @Test fun onResultSaveControl() = fastTest.onResultSaveControl()

    @Test fun onInputTextChange() = fastTest.onInputTextChange(mockk())


    private fun mockDeepCopy(
        item: NoteItem.Text,
        id: Long = Random.nextLong(),
        create: String = nextString(),
        change: String = nextString(),
        name: String = nextString(),
        text: String = nextString(),
        color: Int = Random.nextInt(),
        rankId: Long = Random.nextLong(),
        rankPs: Int = Random.nextInt(),
        isBin: Boolean = Random.nextBoolean(),
        isStatus: Boolean = Random.nextBoolean(),
        alarmId: Long = Random.nextLong(),
        alarmDate: String = nextString()
    ) {
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

        every {
            item.deepCopy(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any()
            )
        } returns item
    }

    private fun MockKVerificationScope.verifyDeepCopy(item: NoteItem.Text) {
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
                any(), any(), any(), any(), any(), any()
        )
    }

}