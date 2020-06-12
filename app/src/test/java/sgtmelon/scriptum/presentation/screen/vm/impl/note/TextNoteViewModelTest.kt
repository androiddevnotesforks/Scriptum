package sgtmelon.scriptum.presentation.screen.vm.impl.note

import io.mockk.MockKVerificationScope
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.IconState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.text.ITextNoteFragment
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
    @MockK lateinit var iconState: IconState

    private val viewModel by lazy { TextNoteViewModel(application) }

    private val fastTest by lazy {
        FastTest.Note.ViewModel(
                callback, parentCallback, interactor, bindInteractor,
                inputControl, saveControl, iconState, viewModel,
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

//        every { viewModel getProperty "saveControl" } returns saveControl

        assertEquals(Default.ID, viewModel.id)
        assertEquals(Default.COLOR, viewModel.color)
        assertTrue(viewModel.rankDialogItemArray.isEmpty())

        assertNotNull(viewModel.callback)
        assertNotNull(viewModel.parentCallback)
    }

    override fun tearDown() {
        super.tearDown()

        confirmVerified(
                callback, parentCallback, interactor, bindInteractor, inputControl,
                saveControl, iconState
        )
    }

    @Test override fun onDestroy() = fastTest.onDestroy()


    @Test fun cacheData() {
        TODO()
    }

    @Test fun onSetup() {
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

    @Test fun onRestoreData() {
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


    private fun mockDeepCopy(item: NoteItem.Text) {
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