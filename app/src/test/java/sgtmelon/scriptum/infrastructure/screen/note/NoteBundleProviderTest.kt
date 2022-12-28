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
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.getEnum
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
        every { bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT) } returns init.isEdit
        FastMock.bundleExtensions()
        every { bundle.getEnum(Intent.STATE, Default.STATE, stateConverter) } returns null

        provider.getData(bundle)
        assertNull(provider.init)

        verifySequence {
            bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
            bundle.getEnum(Intent.STATE, Default.STATE, stateConverter)
        }
    }

    @Test fun `getData with bad type`() {
        every { bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT) } returns init.isEdit
        FastMock.bundleExtensions()
        every { bundle.getEnum(Intent.STATE, Default.STATE, stateConverter) } returns init.noteState
        every { bundle.getLong(Intent.ID, Default.ID) } returns init.id
        every { bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter) } returns null

        provider.getData(bundle)
        assertNull(provider.init)

        verifySequence {
            bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
            bundle.getEnum(Intent.STATE, Default.STATE, stateConverter)
            bundle.getLong(Intent.ID, Default.ID)
            bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter)
        }
    }

    @Test fun `getData with bad color`() {
        every { bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT) } returns init.isEdit
        FastMock.bundleExtensions()
        every { bundle.getEnum(Intent.STATE, Default.STATE, stateConverter) } returns init.noteState
        every { bundle.getLong(Intent.ID, Default.ID) } returns init.id
        every { bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter) } returns init.type
        every { bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter) } returns null
        every { bundle.getString(Intent.NAME, Default.NAME) } returns init.name

        provider.getData(bundle)
        assertEquals(provider.init, init.copy(color = defaultColor))

        verifySequence {
            bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
            bundle.getEnum(Intent.STATE, Default.STATE, stateConverter)
            bundle.getLong(Intent.ID, Default.ID)
            bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter)
            bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter)
            bundle.getString(Intent.NAME, Default.NAME)
        }
    }

    @Test fun `getData with bad name`() {
        every { bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT) } returns init.isEdit
        FastMock.bundleExtensions()
        every { bundle.getEnum(Intent.STATE, Default.STATE, stateConverter) } returns init.noteState
        every { bundle.getLong(Intent.ID, Default.ID) } returns init.id
        every { bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter) } returns init.type
        every { bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter) } returns init.color
        every { bundle.getString(Intent.NAME, Default.NAME) } returns null

        provider.getData(bundle)
        assertEquals(provider.init, init.copy(name = Default.NAME))

        verifySequence {
            bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
            bundle.getEnum(Intent.STATE, Default.STATE, stateConverter)
            bundle.getLong(Intent.ID, Default.ID)
            bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter)
            bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter)
            bundle.getString(Intent.NAME, Default.NAME)
        }
    }

    /**
     * [tearDown] will make a check of [outState] functions not called.
     */
    @Test fun `saveData with null init`() {
        provider.saveData(outState)
    }

    @Test fun `getData and saveData`() {
        TODO()
    }
}