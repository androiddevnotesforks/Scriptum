package sgtmelon.scriptum.basic.automator

import androidx.test.uiautomator.UiDevice
import sgtmelon.scriptum.basic.extension.waitBefore
import kotlin.random.Random

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