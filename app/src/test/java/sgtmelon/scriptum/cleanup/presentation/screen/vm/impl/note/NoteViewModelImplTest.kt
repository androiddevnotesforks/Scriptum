package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note

import android.os.Bundle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.NoteViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Test for [NoteViewModelImpl].
 */
@ExperimentalCoroutinesApi
class NoteViewModelImplTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: INoteActivity
    @MockK lateinit var typeConverter: NoteTypeConverter
    @MockK lateinit var colorConverter: ColorConverter
    @MockK lateinit var preferencesRepo: PreferencesRepo

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy {
        NoteViewModelImpl(callback, typeConverter, colorConverter, preferencesRepo)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, typeConverter, colorConverter, preferencesRepo, bundle)
    }

    override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    //region Help functions

    private fun mockkInit(): Color {
        val color = mockk<Color>()

        every { preferencesRepo.defaultColor } returns color

        assertEquals(viewModel.color, color)

        return color
    }

    private fun verifyInit() {
        preferencesRepo.defaultColor
    }

    //endregion

    @Test fun `onSetup with null bundle`() {
        val initColor = mockkInit()
        val crashlytics = mockk<FirebaseCrashlytics>()

        assertEquals(viewModel.id, Default.ID)
        assertNull(viewModel.type)
        assertEquals(viewModel.color, initColor)

        every { typeConverter.toEnum(Default.TYPE) } returns null

        viewModel.onSetup()

        assertEquals(viewModel.id, Default.ID)
        assertNull(viewModel.type)
        assertEquals(viewModel.color, initColor)

        verifySequence {
            verifyInit()

            typeConverter.toEnum(Default.TYPE)
            callback.finish()
        }
    }

    @Test fun `onSetup with fill bundle but bad data`() {
        val initColor = mockkInit()
        val crashlytics = mockk<FirebaseCrashlytics>()

        every { bundle.getLong(Intent.ID, Default.ID) } returns Default.ID
        every { bundle.getInt(Intent.TYPE, Default.TYPE) } returns Default.TYPE

        every { typeConverter.toEnum(Default.TYPE) } returns null
        FastMock.fireExtensions()
        every { any<IllegalAccessException>().record() } returns mockk()

        assertEquals(viewModel.id, Default.ID)
        assertNull(viewModel.type)
        assertEquals(viewModel.color, initColor)

        viewModel.onSetup(bundle)

        assertEquals(viewModel.id, Default.ID)
        assertNull(viewModel.type)
        assertEquals(viewModel.color, initColor)

        verifySequence {
            verifyInit()

            bundle.getLong(Intent.ID, Default.ID)

            bundle.getInt(Intent.TYPE, Default.TYPE)
            typeConverter.toEnum(Default.TYPE)

            callback.finish()
        }
    }

    @Test fun `onSetup with fill bundle and good data`() {
        val initColor = mockkInit()

        val id = Random.nextLong()
        val type = mockk<NoteType>()
        val typeOrdinal = Random.nextInt()
        val color = mockk<Color>()
        val colorOrdinal = Random.nextInt()

        every { bundle.getLong(Intent.ID, Default.ID) } returns id
        every { bundle.getInt(Intent.TYPE, Default.TYPE) } returns typeOrdinal
        every { typeConverter.toEnum(typeOrdinal) } returns type
        every { bundle.getInt(Intent.COLOR, Default.COLOR) } returns colorOrdinal
        every { colorConverter.toEnum(colorOrdinal) } returns color

        assertEquals(viewModel.id, Default.ID)
        assertNull(viewModel.type)
        assertEquals(viewModel.color, initColor)

        viewModel.onSetup(bundle)

        assertEquals(viewModel.id, id)
        assertEquals(viewModel.type, type)
        assertEquals(viewModel.color, color)

        verifySequence {
            verifyInit()

            bundle.getLong(Intent.ID, Default.ID)
            bundle.getInt(Intent.TYPE, Default.TYPE)
            typeConverter.toEnum(typeOrdinal)
            bundle.getInt(Intent.COLOR, Default.COLOR)
            colorConverter.toEnum(colorOrdinal)
            callback.updateHolder(color)
            callback.setupInsets()
        }
    }

    @Test fun onSaveData() {
        val initColor = mockkInit()
        val initColorOrdinal = Random.nextInt()
        val color = mockk<Color>()
        val colorOrdinal = Random.nextInt()

        val id = Random.nextLong()
        val type = NoteType.values().random()

        every { colorConverter.toInt(initColor) } returns initColorOrdinal

        viewModel.id = id
        viewModel.onSaveData(bundle)

        every { colorConverter.toInt(color) } returns colorOrdinal

        viewModel.type = type
        viewModel.color = color
        viewModel.onSaveData(bundle)

        verifySequence {
            verifyInit()

            bundle.putLong(Intent.ID, id)
            bundle.putInt(Intent.TYPE, Default.TYPE)
            colorConverter.toInt(initColor)
            bundle.putInt(Intent.COLOR, initColorOrdinal)

            bundle.putLong(Intent.ID, id)
            bundle.putInt(Intent.TYPE, type.ordinal)
            colorConverter.toInt(color)
            bundle.putInt(Intent.COLOR, colorOrdinal)
        }
    }

    @Test fun onSetupFragment() {
        mockkInit()

        val id = Random.nextLong()
        val color = mockk<Color>()

        viewModel.id = id
        viewModel.color = color

        viewModel.onSetupFragment(checkCache = false)

        viewModel.type = NoteType.TEXT
        viewModel.onSetupFragment(checkCache = true)

        viewModel.type = NoteType.ROLL
        viewModel.onSetupFragment(checkCache = false)

        verifySequence {
            verifyInit()

            callback.finish()
            callback.showTextFragment(id, color, checkCache = true)
            callback.showRollFragment(id, color, checkCache = false)
        }
    }

    @Test fun onPressBack() {
        mockkInit()

        assertNull(viewModel.type)
        assertFalse(viewModel.onPressBack())

        every { callback.onPressBackText() } returns true

        viewModel.type = NoteType.TEXT
        assertTrue(viewModel.onPressBack())

        every { callback.onPressBackRoll() } returns true

        viewModel.type = NoteType.ROLL
        assertTrue(viewModel.onPressBack())

        verifySequence {
            verifyInit()

            callback.onPressBackText()
            callback.onPressBackRoll()
        }
    }

    @Test fun onUpdateNoteId() {
        mockkInit()

        val id = Random.nextLong()

        assertEquals(viewModel.id, Default.ID)
        viewModel.onUpdateNoteId(id)
        assertEquals(viewModel.id, id)

        verifySequence {
            verifyInit()
        }
    }

    @Test fun onUpdateNoteColor() {
        mockkInit()

        val color = mockk<Color>()

        viewModel.onUpdateNoteColor(color)
        assertEquals(viewModel.color, color)

        verifySequence {
            verifyInit()

            callback.updateHolder(color)
        }
    }

    @Test fun onConvertNote() {
        mockkInit()

        val id = Random.nextLong()
        val color = mockk<Color>()

        viewModel.id = id
        viewModel.color = color

        viewModel.onConvertNote()

        viewModel.type = NoteType.TEXT
        viewModel.onConvertNote()

        assertEquals(viewModel.type, NoteType.ROLL)

        viewModel.type = NoteType.ROLL
        viewModel.onConvertNote()

        assertEquals(viewModel.type, NoteType.TEXT)

        verifySequence {
            verifyInit()

            callback.finish()
            callback.showRollFragment(id, color, checkCache = true)
            callback.showTextFragment(id, color, checkCache = true)
        }
    }
}