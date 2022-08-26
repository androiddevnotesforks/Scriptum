package sgtmelon.scriptum.parent

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity

/**
 * Parent class for tests.
 */
abstract class ParentTest {

    @Before @CallSuper open fun setUp() {
        SplashActivity.isTesting = true
    }

    @After @CallSuper open fun tearDown() {
        SplashActivity.isTesting = false
    }
}