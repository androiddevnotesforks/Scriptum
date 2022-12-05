package sgtmelon.scriptum.parent

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication

/**
 * Parent class for tests.
 */
abstract class ParentTest {

    @Before @CallSuper open fun setUp() {
        ScriptumApplication.isTesting = true
    }

    @After @CallSuper open fun tearDown() {
        ScriptumApplication.isTesting = false
    }
}