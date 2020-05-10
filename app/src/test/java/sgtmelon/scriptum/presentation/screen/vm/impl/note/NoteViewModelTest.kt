package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.os.Bundle
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.data.NoteData.Intent
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteActivity
import kotlin.random.Random

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

    @Test fun onSetup_nullBundle() {
        TODO("nullable")

        val color = Color.RED
        val theme = Theme.DARK

        every { interactor.defaultColor } returns color
        every { interactor.theme } returns theme

        viewModel.onSetup()

        assertEquals(Default.ID, viewModel.id)
        assertEquals(color, viewModel.color)
        assertEquals(null, viewModel.type)

        verifySequence {
            interactor.defaultColor
            interactor.theme
            callback.updateHolder(theme, color)
        }
    }

    @Test fun onSetup_nullBundle_nullTheme() {
        TODO("nullable")

        val color = Color.RED

        every { interactor.defaultColor } returns color
        every { interactor.theme } returns null

        viewModel.onSetup()

        assertEquals(Default.ID, viewModel.id)
        assertEquals(color, viewModel.color)
        assertEquals(null, viewModel.type)

        verifySequence {
            interactor.defaultColor
            interactor.theme
        }
    }

    @Test fun onSetup_fillBundle_badData() {
        TODO("nullable")

        val color = Color.ORANGE
        val theme = Theme.LIGHT

        every { bundle.getLong(Intent.ID, Default.ID) } returns Default.ID
        every { bundle.getInt(Intent.COLOR, Default.COLOR) } returns Default.COLOR
        every { bundle.getInt(Intent.TYPE, Default.TYPE) } returns Default.TYPE

        every { interactor.defaultColor } returns color
        every { interactor.theme } returns theme

        viewModel.onSetup(bundle)

        assertEquals(Default.ID, viewModel.id)
        assertEquals(color, viewModel.color)
        assertEquals(null, viewModel.type)

        verifySequence {
            bundle.getLong(Intent.ID, Default.ID)
            bundle.getInt(Intent.COLOR, Default.COLOR)
            bundle.getInt(Intent.TYPE, Default.TYPE)

            interactor.defaultColor
            interactor.theme
            callback.updateHolder(theme, color)
        }
    }

    @Test fun onSetup_fillBundle_goodData() {
        TODO("nullable")

        val id = Random.nextLong()
        val color = Color.TEAL
        val theme = Theme.DARK

        every { bundle.getLong(Intent.ID, Default.ID) } returns id
        every { bundle.getInt(Intent.COLOR, Default.COLOR) } returns color
        every { bundle.getInt(Intent.TYPE, Default.TYPE) } returns NoteType.TEXT.ordinal

        every { interactor.theme } returns theme

        viewModel.onSetup(bundle)

        verifySequence {
            bundle.getLong(Intent.ID, Default.ID)
            bundle.getInt(Intent.COLOR, Default.COLOR)
            bundle.getInt(Intent.TYPE, Default.TYPE)

            interactor.theme
            callback.updateHolder(theme, color)
        }
    }


    @Test fun onSaveData() {
        val id = Random.nextLong()
        val color = Color.BLUE
        val type = NoteType.TEXT

        every { bundle.putLong(Intent.ID, any()) } returns Unit
        every { bundle.putInt(Intent.COLOR, any()) } returns Unit
        every { bundle.putInt(Intent.TYPE, any()) } returns Unit

        viewModel.id = id
        viewModel.color = color

        viewModel.onSaveData(bundle)

        viewModel.type = type
        viewModel.onSaveData(bundle)

        verifySequence {
            bundle.putLong(Intent.ID, id)
            bundle.putInt(Intent.COLOR, color)
            bundle.putInt(Intent.TYPE, Default.TYPE)

            bundle.putLong(Intent.ID, id)
            bundle.putInt(Intent.COLOR, color)
            bundle.putInt(Intent.TYPE, type.ordinal)
        }
    }

    @Test fun onSetupFragment() {
        val id = Random.nextLong()
        val color = Color.BROWN

        viewModel.id = id
        viewModel.color = color

        viewModel.onSetupFragment(checkCache = false)

        viewModel.type = NoteType.TEXT
        viewModel.onSetupFragment(checkCache = true)

        viewModel.type = NoteType.ROLL
        viewModel.onSetupFragment(checkCache = false)

        verifySequence {
            callback.finish()
            callback.showTextFragment(id, color, checkCache = true)
            callback.showRollFragment(id, color, checkCache = false)
        }
    }

    @Test fun onPressBack() {
        assertEquals(null, viewModel.type)
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
        val id = Random.nextLong()

        assertEquals(Default.ID, viewModel.id)
        viewModel.onUpdateNoteId(id)
        assertEquals(id, viewModel.id)
    }

    @Test fun onUpdateNoteColor() {
        TODO("nullable")

        val firstColor = Color.BLUE
        val secondColor = Color.RED

        every { interactor.theme } returns null

        assertEquals(Default.COLOR, viewModel.color)
        viewModel.onUpdateNoteColor(firstColor)

        every { interactor.theme } returns Theme.DARK

        assertEquals(firstColor, viewModel.color)
        viewModel.onUpdateNoteColor(secondColor)

        assertEquals(secondColor, viewModel.color)
        verifySequence {
            callback.updateHolder(Theme.DARK, secondColor)
        }
    }

    @Test fun onConvertNote() {
        val id = Random.nextLong()
        val color = Color.ORANGE

        viewModel.id = id
        viewModel.color = color

        viewModel.onConvertNote()

        viewModel.type = NoteType.TEXT
        viewModel.onConvertNote()

        assertEquals(NoteType.ROLL, viewModel.type)

        viewModel.type = NoteType.ROLL
        viewModel.onConvertNote()

        assertEquals(NoteType.TEXT, viewModel.type)

        verifySequence {
            callback.finish()
            callback.showRollFragment(id, color, checkCache = true)
            callback.showTextFragment(id, color, checkCache = true)
        }
    }

}