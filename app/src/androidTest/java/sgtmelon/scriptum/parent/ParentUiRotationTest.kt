package sgtmelon.scriptum.parent

import org.junit.After
import sgtmelon.scriptum.cleanup.basic.automator.RotateAutomator

/**
 * Parent class for Rotation tests
 *
 * For rotate screen use: ctrl + left/rightArrow
 */
abstract class ParentUiRotationTest : ParentUiTest() {

    protected val automator by lazy { RotateAutomator(uiDevice) }

    @After override fun tearDown() {
        super.tearDown()
        automator.rotateNatural()
    }
}