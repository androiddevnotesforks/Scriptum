package sgtmelon.scriptum.test

import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.basic.automator.RotateAutomator

/**
 * Parent class for Rotation tests
 *
 * For rotate screen use: ctrl + left/rightArrow
 */
abstract class ParentRotationTest : ParentUiTest() {

    protected var automator: RotateAutomator? = null

    @Before override fun setup() {
        super.setup()

        automator = RotateAutomator(uiDevice)
    }

    @After override fun tearDown() {
        super.tearDown()

        automator?.rotateNatural()
        automator = null
    }
}