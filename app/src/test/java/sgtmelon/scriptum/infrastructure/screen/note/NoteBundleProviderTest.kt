package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.getEnum
import sgtmelon.scriptum.infrastructure.utils.extensions.putEnum
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [NoteBundleProvider].
 */
class NoteBundleProviderTest : ParentTest() {

    @MockK lateinit var defaultColor: Color
    @MockK lateinit var typeConverter: NoteTypeConverter
    @MockK lateinit var colorConverter: ColorConverter
    @MockK lateinit var stateConverter: NoteStateConverter

    @MockK lateinit var bundle: Bundle
    @MockK lateinit var outState: Bundle

    private val init = NoteInit(
        Random.nextBoolean(), mockk(), Random.nextLong(), mockk(), mockk(), nextString()
    )

    private val provider by lazy {
        NoteBundleProvider(defaultColor, typeConverter, colorConverter, stateConverter)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            defaultColor, typeConverter, colorConverter, stateConverter,
            bundle, outState
        )
    }

    @Test fun `getData with null bundle`() {
        provider.getData(bundle = null)
        assertNull(provider.init)
    }

    @Test fun `getData with bad noteState`() {
        with(init) { mockkGetData(isEdit, noteState = null, id, type, color, name) }

        provider.getData(bundle)
        assertNull(provider.init)

        verifySequence {
            verifyGetData(noteState = null, init.type)
        }
    }

    @Test fun `getData with bad type`() {
        with(init) { mockkGetData(isEdit, noteState, id, type = null, color, name) }

        provider.getData(bundle)
        assertNull(provider.init)

        verifySequence {
            verifyGetData(init.noteState, type = null)
        }
    }

    @Test fun `getData with bad color`() {
        with(init) { mockkGetData(isEdit, noteState, id, type, color = null, name) }

        provider.getData(bundle)
        assertEquals(provider.init, init.copy(color = defaultColor))

        verifySequence {
            verifyGetData(init.noteState, init.type)
        }
    }

    @Test fun `getData with bad name`() {
        with(init) { mockkGetData(isEdit, noteState, id, type, color, name = null) }

        provider.getData(bundle)
        assertEquals(provider.init, init.copy(name = Default.NAME))

        verifySequence {
            verifyGetData(init.noteState, init.type)
        }
    }

    /**
     * [tearDown] will make a check of [outState] functions not called.
     */
    @Test fun `saveData with null init`() {
        provider.saveData(outState)
    }

    @Test fun `getData and saveData`() {
        with(init) {
            mockkGetData(isEdit, noteState, id, type, color, name)

            every { outState.putBoolean(Intent.IS_EDIT, isEdit) } returns Unit
            every { outState.putEnum(Intent.STATE, stateConverter, noteState) } returns Unit
            every { outState.putLong(Intent.ID, id) } returns Unit
            every { outState.putEnum(Intent.TYPE, typeConverter, type) } returns Unit
            every { outState.putEnum(Intent.COLOR, colorConverter, color) } returns Unit
            every { outState.putString(Intent.NAME, name) } returns Unit
        }

        provider.getData(bundle)
        provider.saveData(outState)

        verifySequence {
            verifyGetData(init.noteState, init.type)

            outState.putBoolean(Intent.IS_EDIT, init.isEdit)
            outState.putEnum(Intent.STATE, stateConverter, init.noteState)
            outState.putLong(Intent.ID, init.id)
            outState.putEnum(Intent.TYPE, typeConverter, init.type)
            outState.putEnum(Intent.COLOR, colorConverter, init.color)
            outState.putString(Intent.NAME, init.name)
        }
    }

    private fun mockkGetData(
        isEdit: Boolean,
        noteState: NoteState?,
        id: Long,
        type: NoteType?,
        color: Color?,
        name: String?
    ) {
        every { bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT) } returns isEdit
        FastMock.bundleExtensions()
        every { bundle.getEnum(Intent.STATE, Default.STATE, stateConverter) } returns noteState
        every { bundle.getLong(Intent.ID, Default.ID) } returns id
        every { bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter) } returns type
        every { bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter) } returns color
        every { bundle.getString(Intent.NAME, Default.NAME) } returns name
    }

    private fun verifyGetData(noteState: NoteState?, type: NoteType?) {
        bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
        bundle.getEnum(Intent.STATE, Default.STATE, stateConverter)

        if (noteState == null) return

        bundle.getLong(Intent.ID, Default.ID)
        bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter)

        if (type == null) return

        bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter)
        bundle.getString(Intent.NAME, Default.NAME)
    }
}