package sgtmelon.scriptum.test

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import sgtmelon.scriptum.basic.automator.RotateAutomator

/**
 * Parent class for Rotation tests
 *
 * For rotate screen use: ctrl + left/rightArrow
 */
abstract class ParentRotationTest : ParentUiTest() {

    protected var automator: RotateAutomator? = null

    override fun setUp() {
        super.setUp()

        automator = RotateAutomator(getInstrumentation())
    }

    override fun tearDown() {
        super.tearDown()

        automator?.rotateNatural()
        automator = null
    }
}