package sgtmelon.scriptum.infrastructure.converter

import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.utils.extensions.record
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [UriConverter].
 */
class UriConverterTest : ParentTest() {

    private val converter = UriConverter()

    @Test fun toUri() {
        val value = nextString()
        val uri = mockk<Uri>()

        mockkStatic(Uri::class)
        every { Uri.parse(value) } returns uri

        assertEquals(converter.toUri(value), uri)

        verifySequence {
            Uri.parse(value)
        }
    }

    @Test fun `toUri with throw`() {
        val value = nextString()
        val exception = mockk<Exception>()

        mockkStatic(Uri::class)
        every { Uri.parse(value) } throws exception
        FastMock.fireExtensions()
        every { exception.record() } returns mockk()

        assertNull(converter.toUri(value))

        verifySequence {
            Uri.parse(value)
            exception.record()
        }
    }

    @Test fun `uri toString`() {
        val uri = mockk<Uri>()
        val result = nextString()

        every { uri.toString() } returns result

        assertEquals(converter.toString(uri), result)

        verifySequence {
            uri.toString()
        }
    }
}