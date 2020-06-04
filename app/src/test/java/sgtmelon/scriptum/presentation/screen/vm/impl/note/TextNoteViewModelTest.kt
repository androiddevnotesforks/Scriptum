package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.os.Bundle
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.data.NoteData.Intent
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.presentation.screen.ui.callback.note.text.ITextNoteFragment
import kotlin.random.Random

/**
 * Test for [TextNoteViewModel].
 */
@ExperimentalCoroutinesApi
class TextNoteViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: ITextNoteFragment
    @MockK lateinit var parentCallback: INoteChild

    @MockK lateinit var interactor: ITextNoteInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    @MockK lateinit var inputControl: IInputControl

    private val viewModel by lazy { TextNoteViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setParentCallback(parentCallback)
        viewModel.setInteractor(interactor, bindInteractor)
        viewModel.setInputControl(inputControl)
    }

    override fun onDestroy() {
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


    @Test fun setParentCallback() {
        TODO()
    }

    @Test fun setInteractor() {
        TODO()
    }

    @Test fun onSetup() {
        TODO()
    }


    @Test fun onSaveData() {
        assertEquals(Default.ID, viewModel.id)
        assertEquals(Default.COLOR, viewModel.color)

        val id = Random.nextLong()
        val color = Random.nextInt()
        val bundle = mockk<Bundle>()

        every { bundle.putLong(Intent.ID, id) } returns Unit
        every { bundle.putInt(Intent.COLOR, color) } returns Unit

        viewModel.id = id
        viewModel.color = color
        viewModel.onSaveData(bundle)

        verifySequence {
            bundle.putLong(Intent.ID, id)
            bundle.putInt(Intent.COLOR, color)
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

    @Test fun onRestoreData() {
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
        val noteItem = mockk<NoteItem.Text>()
        val restoreItem = mockk<NoteItem.Text>()

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
        TODO()
    }

    @Test fun onMenuBind() {
        TODO()
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
        val noteItem = mockk<NoteItem.Text>()
        val access = mockk<InputControl.Access>()

        every { inputControl.access } returns access

        viewModel.noteItem = noteItem
        viewModel.onInputTextChange()

        verifySequence {
            inputControl.access
            callback.onBindingInput(noteItem, access)
        }
    }

}