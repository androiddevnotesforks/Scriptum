package sgtmelon.test.cappuccino.automator

import androidx.test.uiautomator.UiDevice
import kotlin.random.Random
import sgtmelon.test.cappuccino.utils.await

class RotateAutomator(private val uiDevice: UiDevice) {

    fun toSide() {
        if (Random.nextBoolean()) {
            uiDevice.setOrientationLeft()
        } else {
            uiDevice.setOrientationRight()
        }

        await(time = 2000)
    }

    fun toNormal() {
        uiDevice.setOrientationNatural()

        await(time = 2000)
    }
}