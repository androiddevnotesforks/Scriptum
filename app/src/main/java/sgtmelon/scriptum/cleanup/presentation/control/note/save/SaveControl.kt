package sgtmelon.scriptum.cleanup.presentation.control.note.save

import android.content.res.Resources
import android.os.Handler
import sgtmelon.scriptum.R
import sgtmelon.common.test.annotation.RunPrivate

/**
 * Class for help control note pause/auto save.
 */
class SaveControl(
    resources: Resources,
    val model: Model,
    private val callback: Callback
) : ISaveControl {

    @RunPrivate var saveHandler = Handler()

    @RunPrivate val periodTime: Int? = if (model.autoSaveOn) {
        resources.getIntArray(R.array.pref_note_save_time_array).getOrNull(model.savePeriod)
    } else {
        null
    }

    /**
     * onPause happen not only if application close (e.g. if we close activity).
     */
    override var needSave = true

    override fun setSaveEvent(isWork: Boolean) {
        if (!model.autoSaveOn) return

        saveHandler.removeCallbacksAndMessages(null)

        if (isWork) {
            val period = periodTime?.toLong() ?: return
            saveHandler.postDelayed({ onSaveRunnable() }, period)
        }
    }

    @RunPrivate fun onSaveRunnable() {
        callback.onResultSaveControl()
        setSaveEvent(true)
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

    interface Setup {
        fun getSaveModel(): Model
    }
}