package sgtmelon.scriptum.infrastructure.screen.alarm

import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
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

    @Test fun getNoteId() {
        TODO()
    }

    @Test fun getData() {
        TODO()
    }

    @Test fun saveData() {
        TODO()
    }
}