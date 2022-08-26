package sgtmelon.scriptum.parent

import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity

/**
 * Parent class for Integration tests.
 */
abstract class ParentIntegrationTest : ParentTest() {

    @Before override fun setUp() {
        super.setUp()
        SplashActivity.isTesting = true
    }

    @After override fun tearDown() {
        super.tearDown()
        SplashActivity.isTesting = false
    }
}