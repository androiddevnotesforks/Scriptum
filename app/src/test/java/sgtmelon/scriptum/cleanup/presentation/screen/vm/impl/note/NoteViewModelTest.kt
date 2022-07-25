package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note.Default
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.parent.ParentViewModelTest

/**
 * Test for [NoteViewModel].
 */
@ExperimentalCoroutinesApi
class NoteViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: INoteActivity
    @MockK lateinit var interactor: INoteInteractor

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { NoteViewModel(callback, interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, bundle)
    }

    override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup_nullBundle() {
        val color = Random.nextInt()

        every { interactor.defaultColor } returns color

        assertEquals(Default.ID, viewModel.id)
        assertEquals(Default.COLOR, viewModel.color)
        assertEquals(null, viewModel.type)

        viewModel.onSetup()

        assertEquals(Default.ID, viewModel.id)
        assertEquals(color, viewModel.color)
        assertEquals(null, viewModel.type)

        verifySequence {
            interactor.defaultColor
            callback.updateHolder(color)
            callback.setupInsets()
        }
    }

    @Test fun onSetup_fillBundle_badData() {
        val color = Random.nextInt()

        every { bundle.getLong(Intent.ID, Default.ID) } returns Default.ID
        every { bundle.getInt(Intent.COLOR, Default.COLOR) } returns Default.COLOR
        every { bundle.getInt(Intent.TYPE, Default.TYPE) } returns Default.TYPE

        every { interactor.defaultColor } returns color

        assertEquals(Default.ID, viewModel.id)
        assertEquals(Default.COLOR, viewModel.color)
        assertEquals(null, viewModel.type)

        viewModel.onSetup(bundle)

        assertEquals(Default.ID, viewModel.id)
        assertEquals(color, viewModel.color)
        assertEquals(null, viewModel.type)

        verifySequence {
            bundle.getLong(Intent.ID, Default.ID)
            bundle.getInt(Intent.COLOR, Default.COLOR)
            bundle.getInt(Intent.TYPE, Default.TYPE)
            interactor.defaultColor
            callback.updateHolder(color)
            callback.setupInsets()
        }
    }

    @Test fun onSetup_fillBundle_goodData() {
        val id = Random.nextLong()
        val color = Random.nextInt()
        val type = NoteType.TEXT

        every { bundle.getLong(Intent.ID, Default.ID) } returns id
        every { bundle.getInt(Intent.COLOR, Default.COLOR) } returns color
        every { bundle.getInt(Intent.TYPE, Default.TYPE) } returns type.ordinal

        assertEquals(Default.ID, viewModel.id)
        assertEquals(Default.COLOR, viewModel.color)
        assertNull(viewModel.type)

        viewModel.onSetup(bundle)

        assertEquals(id, viewModel.id)
        assertEquals(color, viewModel.color)
        assertEquals(type, viewModel.type)

        verifySequence {
            bundle.getLong(Intent.ID, Default.ID)
            bundle.getInt(Intent.COLOR, Default.COLOR)
            bundle.getInt(Intent.TYPE, Default.TYPE)
            callback.updateHolder(color)
            callback.setupInsets()
        }
    }


    @Test fun onSaveData() {
        val id = Random.nextLong()
        val color = Random.nextInt()
        val type = NoteType.TEXT

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
        val color = Random.nextInt()

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
        val color = Random.nextInt()

        viewModel.onUpdateNoteColor(color)
        assertEquals(color, viewModel.color)

        verifySequence {
            callback.updateHolder(color)
        }
    }

    @Test fun onConvertNote() {
        val id = Random.nextLong()
        val color = Random.nextInt()

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