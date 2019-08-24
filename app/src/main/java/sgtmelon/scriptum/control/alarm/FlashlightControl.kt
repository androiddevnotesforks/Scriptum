package sgtmelon.scriptum.control.alarm

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import sgtmelon.scriptum.control.alarm.callback.IFlashlightControl

/**
 * Class for help control []
 *
 * @author SerjantArbuz
 */
class FlashlightControl(context: Context?) : IFlashlightControl {

    // TODO #RELEASE1 coroutine

    // private val flashlightControl: IFlashlightControl by lazy { FlashlightControl(context = this) }

    private val useNewApi = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    @RequiresApi(Build.VERSION_CODES.M)
    private val cameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager


    override fun toggle() {
//        cameraManager?.getCameraIdList()?.forEach {
//            val characteristic = cameraManager.getCameraCharacteristics(it)
//
//            val available: Boolean = characteristic.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
//
//            if (available) cameraManager.setTorchMode(it, turnOn)
//        }

        val camera = Camera.open()
        val params = camera.parameters
        params.flashMode = Camera.Parameters.FLASH_MODE_TORCH
        camera.parameters = params
        camera.startPreview()
    }

    override fun turnOn() {
        if (useNewApi) {

        } else {

        }
    }

    override fun turnOff() {
        if (useNewApi) {

        } else {

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun switchNew(turnOn: Boolean) {
        cameraManager?.cameraIdList?.forEach {
            val char = cameraManager.getCameraCharacteristics(it)

            if (!char.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) return@forEach

            cameraManager.setTorchMode(it, turnOn)
        }
    }

    private fun switchOld(turnOn: Boolean) {

    }

}