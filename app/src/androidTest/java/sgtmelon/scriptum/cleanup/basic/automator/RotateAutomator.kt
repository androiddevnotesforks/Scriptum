package sgtmelon.scriptum.cleanup.basic.automator

import androidx.test.uiautomator.UiDevice
import kotlin.random.Random
import sgtmelon.scriptum.cleanup.basic.extension.waitBefore

class RotateAutomator(private val uiDevice: UiDevice) {

    fun rotateSide() {
        if (Random.nextBoolean()) {
            uiDevice.setOrientationLeft()
        } else {
            uiDevice.setOrientationRight()
        }

        waitBefore(time = 2000)
    }

    fun rotateNatural() {
        uiDevice.setOrientationNatural()

        waitBefore(time = 2000)
    }

}