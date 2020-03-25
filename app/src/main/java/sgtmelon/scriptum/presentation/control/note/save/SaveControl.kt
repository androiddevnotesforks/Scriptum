package sgtmelon.scriptum.presentation.control.note.save

import android.content.Context
import android.os.Handler
import sgtmelon.scriptum.R

/**
 * Class for help control note pause/auto save.
 */
class SaveControl(context: Context, private val model: Model, private val callback: Callback) :
        ISaveControl {

    private val saveHandler = Handler()

    private val saveTime: Int = if (model.autoSaveOn) {
        context.resources.getIntArray(R.array.pref_note_save_time_array)[model.savePeriod]
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
            saveHandler.postDelayed(saveRunnable, saveTime.toLong())
        }
    }

    override fun onPauseSave() {
        if (!model.pauseSaveOn) return

        if (needSave) {
            callback.onResultSaveControl()
        }
    }

    class Model(val pauseSaveOn: Boolean, val autoSaveOn: Boolean, val savePeriod: Int)

    interface Callback {
        fun onResultSaveControl()
    }

}