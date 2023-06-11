package sgtmelon.scriptum.source.ui.tests

import org.junit.Before
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication

/**
 * Parent class for test "control" cases, when developer need to see whats happen on device screen
 * (like animation, transition).
 */
abstract class ParentUiControlTest : ParentUiTest() {

    @Before override fun setUp() {
        super.setUp()
        ScriptumApplication.skipAnimation = false
    }
}