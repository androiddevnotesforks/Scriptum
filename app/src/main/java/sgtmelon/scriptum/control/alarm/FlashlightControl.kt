package sgtmelon.scriptum.control.alarm

import android.content.Context
import android.hardware.camera2.CameraManager
import sgtmelon.scriptum.control.alarm.callback.IFlashlightControl

/**
 * Class for help control []
 *
 * @author SerjantArbuz
 */
class FlashlightControl(context: Context?) : IFlashlightControl {

    // TODO #RELEASE1 coroutine

    // private val flashlightControl: IFlashlightControl by lazy { FlashlightControl(context = this) }

    private val cameraManager = context?.getSystemService(Context.CAMERA_SERVICE)
            as? CameraManager

    override fun toggle() {
        turnOn = !turnOn

        cameraManager?.getCameraIdList()?.forEach {
            try {
                cameraManager.setTorchMode(it, turnOn)
            } catch (e: Exception) {

            }
        }
    }

    companion object {
        var turnOn = false
    }

}