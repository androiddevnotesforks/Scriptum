package sgtmelon.scriptum.cleanup.basic.automator

import androidx.test.uiautomator.UiDevice
import kotlin.random.Random
import sgtmelon.scriptum.cleanup.basic.extension.sleep

class RotateAutomator(private val uiDevice: UiDevice) {

    fun toSide() {
        if (Random.nextBoolean()) {
            uiDevice.setOrientationLeft()
        } else {
            uiDevice.setOrientationRight()
        }

        sleep(time = 2000)
    }

    fun toNormal() {
        uiDevice.setOrientationNatural()

        sleep(time = 2000)
    }

}