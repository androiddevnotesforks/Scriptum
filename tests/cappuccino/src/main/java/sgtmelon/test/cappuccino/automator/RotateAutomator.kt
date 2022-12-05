package sgtmelon.test.cappuccino.automator

import androidx.test.uiautomator.UiDevice
import kotlin.random.Random
import sgtmelon.test.cappuccino.utils.await

class RotateAutomator(private val uiDevice: UiDevice) {

    private var isNormal: Boolean = true

    fun toSide() = with(uiDevice) {
        isNormal = false
        if (Random.nextBoolean()) setOrientationLeft() else setOrientationRight()
        await(time = 2000)
    }

    fun toNormal() {
        isNormal = true
        uiDevice.setOrientationNatural()
        await(time = 2000)
    }

    fun switch() = if (isNormal) toSide() else toNormal()

}