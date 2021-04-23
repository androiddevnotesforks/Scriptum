package sgtmelon.scriptum.test

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

        automator = RotateAutomator(uiDevice)
    }

    override fun tearDown() {
        super.tearDown()

        automator?.rotateNatural()
        automator = null
    }
}