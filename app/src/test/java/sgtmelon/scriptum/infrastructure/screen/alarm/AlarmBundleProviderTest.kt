package sgtmelon.scriptum.infrastructure.screen.alarm

import android.os.Bundle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.exception.BundleException
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [AlarmBundleProvider].
 */
class AlarmBundleProviderTest : ParentTest() {

    private val bundleProvider = AlarmBundleProvider()

    @Before override fun setUp() {
        super.setUp()
        assertNull(bundleProvider.noteId)
    }

    @Test fun `getData with null bundle`() {
        FastMock.fireExtensions()
        every { any<BundleException>().record() } returns mockk()

        bundleProvider.getData(bundle = null)
        assertNull(bundleProvider.noteId)
    }

    @Test fun `getData and save`() {
        val bundle = mockk<Bundle>()
        val outState = mockk<Bundle>()
        val noteId = Random.nextLong()

        every { bundle.getLong(Intent.ID, Default.ID) } returns noteId
        every { outState.putLong(Intent.ID, noteId) } returns Unit

        bundleProvider.getData(bundle)
        Assert.assertEquals(bundleProvider.noteId, noteId)
        bundleProvider.saveData(outState)

        verifySequence {
            bundle.getLong(Intent.ID, Default.ID)
            outState.putLong(Intent.ID, noteId)
        }
    }
}