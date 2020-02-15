package sgtmelon.scriptum.screen.vm.note

import android.os.Bundle
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

import org.junit.Assert.*
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Test for [NoteViewModel]
 */
@ExperimentalCoroutinesApi
class NoteViewModelTest : ParentViewModelTest() {

    private val data = TestData.Note

    @MockK lateinit var callback: INoteActivity

    @MockK lateinit var interactor: INoteInteractor

    private val bundle = mockkClass(Bundle::class)

    private val viewModel by lazy { NoteViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        TODO()
    }

    @Test fun onSaveData() {
        TODO()
    }

    @Test fun onSetupFragment() {
        TODO()
    }

    @Test fun onPressBack() {
        viewModel.type = null
        assertFalse(viewModel.onPressBack())

        every { callback.onPressBackText() } returns true

        viewModel.type = NoteType.TEXT
        assertTrue(viewModel.onPressBack())

        every { callback.onPressBackRoll() } returns true

        viewModel.type = NoteType.ROLL
        assertTrue(viewModel.onPressBack())

        verifySequence {
            callback.onPressBackText()
            callback.onPressBackRoll()
        }
    }

    @Test fun onUpdateNoteId() {
        TODO()
    }

    @Test fun onUpdateNoteColor() {
        val theme = Theme.DARK
        val color = Color.RED

        every { interactor.theme } returns theme

        viewModel.onUpdateNoteColor(color)

        assertEquals(color, viewModel.color)
        verifySequence { callback.updateHolder(theme, color) }
    }

    @Test fun onConvertNote() {
        TODO()
    }

}