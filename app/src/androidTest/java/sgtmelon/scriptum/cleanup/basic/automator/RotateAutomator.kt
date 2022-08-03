package sgtmelon.scriptum.cleanup.basic.automator

import androidx.test.uiautomator.UiDevice
import kotlin.random.Random
import sgtmelon.scriptum.cleanup.basic.extension.sleep

class RotateAutomator(private val uiDevice: UiDevice) {

    fun rotateSide() {
        if (Random.nextBoolean()) {
            uiDevice.setOrientationLeft()
        } else {
            uiDevice.setOrientationRight()
        }

        sleep(time = 2000)
    }

    fun rotateNatural() {
        uiDevice.setOrientationNatural()

        sleep(time = 2000)
    }

}