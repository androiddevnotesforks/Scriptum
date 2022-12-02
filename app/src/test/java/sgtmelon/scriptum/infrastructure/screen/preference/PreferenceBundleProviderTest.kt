package sgtmelon.scriptum.infrastructure.screen.preference

import android.os.Bundle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.math.abs
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Intent
import sgtmelon.scriptum.infrastructure.model.exception.BundleException
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.utils.record
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

    @Test fun `getData with null bundle`() {
        FastMock.fireExtensions()
        every { any<BundleException>().record() } returns mockk()

        bundleProvider.getData(bundle = null)
        assertNull(bundleProvider.screen)
    }

    @Test fun `getData with wrong ordinal`() {
        val bundle = mockk<Bundle>()
        val ordinal = -abs(Random.nextInt())

        every { bundle.getInt(Intent.SCREEN, Default.SCREEN) } returns ordinal
        FastMock.fireExtensions()
        every { any<BundleException>().record() } returns mockk()

        bundleProvider.getData(bundle)
        assertNull(bundleProvider.screen)

        verifySequence {
            bundle.getInt(Intent.SCREEN, Default.SCREEN)
        }
    }

    @Test fun `getData and save`() {
        val bundle = mockk<Bundle>()
        val outState = mockk<Bundle>()
        val screen = PreferenceScreen.values().random()

        every { bundle.getInt(Intent.SCREEN, Default.SCREEN) } returns screen.ordinal
        every { outState.putInt(Intent.SCREEN, screen.ordinal) } returns Unit

        bundleProvider.getData(bundle)
        assertEquals(bundleProvider.screen, screen)
        bundleProvider.saveData(outState)

        verifySequence {
            bundle.getInt(Intent.SCREEN, Default.SCREEN)
            outState.putInt(Intent.SCREEN, screen.ordinal)
        }
    }
}