package sgtmelon.scriptum.infrastructure.screen.preference

import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [PreferenceBundleProvider].
 */
class PreferenceBundleProviderTest : ParentTest() {

    private val bundleProvider = PreferenceBundleProvider()

    @Before override fun setUp() {
        super.setUp()
        assertNull(bundleProvider.screen)
    }

    @Test fun getScreen() {
        TODO()
    }

    @Test fun getData() {
        TODO()
    }

    @Test fun saveData() {
        TODO()
    }
}