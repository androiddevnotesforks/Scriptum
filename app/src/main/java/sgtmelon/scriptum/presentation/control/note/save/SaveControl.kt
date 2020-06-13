package sgtmelon.scriptum.presentation.control.note.save

import android.content.Context
import android.os.Handler
import sgtmelon.scriptum.R

/**
 * Class for help control note pause/auto save.
 */
class SaveControl(context: Context, val model: Model, private val callback: Callback) :
        ISaveControl {

    private val saveHandler = Handler()

    private val periodTime: Int = if (model.autoSaveOn) {
        val intArray = context.resources.getIntArray(R.array.pref_note_save_time_array)
        intArray.getOrNull(model.savePeriod) ?: 0
    } else {
        0
    }

    private val saveRunnable = {
        callback.onResultSaveControl()
        setSaveEvent(true)
    }

    /**
     * onPause happen not only if application close (e.g. if we close activity).
     */
    override var needSave = true

    override fun setSaveEvent(isWork: Boolean) {
        if (!model.autoSaveOn) return

        saveHandler.removeCallbacksAndMessages(null)

        if (isWork) {
            saveHandler.postDelayed(saveRunnable, periodTime.toLong())
        }
    }

    override fun onPauseSave() {
        if (!model.pauseSaveOn) return

        if (needSave) {
            callback.onResultSaveControl()
        }
    }

    data class Model(val pauseSaveOn: Boolean, val autoSaveOn: Boolean, val savePeriod: Int)

    interface Callback {
        fun onResultSaveControl()
    }

}