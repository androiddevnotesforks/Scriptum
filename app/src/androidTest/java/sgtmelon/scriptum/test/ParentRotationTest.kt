package sgtmelon.scriptum.test

import org.junit.After
import sgtmelon.scriptum.basic.automator.RotateAutomator

/**
 * Parent class for Rotation tests
 *
 * For rotate screen use: ctrl + left/rightArrow
 */
abstract class ParentRotationTest : ParentUiTest() {

    protected val automator by lazy { RotateAutomator(uiDevice) }

    @After override fun tearDown() {
        super.tearDown()
        automator.rotateNatural()
    }
}