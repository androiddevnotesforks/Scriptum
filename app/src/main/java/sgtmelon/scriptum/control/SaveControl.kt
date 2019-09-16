package sgtmelon.scriptum.control

import android.content.Context
import android.os.Handler
import sgtmelon.scriptum.R

/**
 * Class for help control note pause/auto save
 */
class SaveControl(context: Context, private val model: Model, private val callback: Callback) {

    private val saveHandler = Handler()

    private val saveTime: Int = if (model.autoSaveOn) {
        context.resources.getIntArray(R.array.value_save_time_array)[model.savePeriod]
    } else {
        0
    }

    private val saveRunnable = {
        callback.onResultSaveControl()
        setSaveHandlerEvent(true)
    }

    /**
     * onPause cause not only if application turn (e.g. if we close activity)
     */
    var needSave = true

    fun setSaveHandlerEvent(isStart: Boolean) {
        if (!model.autoSaveOn) return

        if (isStart) {
            saveHandler.postDelayed(saveRunnable, saveTime.toLong())
        } else {
            saveHandler.removeCallbacks(saveRunnable)
        }
    }

    fun onPauseSave(editMode: Boolean) {
        setSaveHandlerEvent(false)

        if (needSave && editMode && model.pauseSaveOn) {
            callback.onResultSaveControl()
        } else {
            needSave = true
        }
    }

    class Model(val pauseSaveOn: Boolean, val autoSaveOn: Boolean, val savePeriod: Int)

    interface Callback {
        fun onResultSaveControl()
    }

}