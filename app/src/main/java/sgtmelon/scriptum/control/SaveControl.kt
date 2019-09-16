package sgtmelon.scriptum.control

import android.content.Context
import android.os.Handler

import sgtmelon.scriptum.R
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Класс контроля сохранений заметки
 */
class SaveControl(context: Context, private val callback: Callback) {

    // TODO вынести отсюда PreferenceRepo

    private val saveHandler = Handler()

    private val isPauseSaveOn: Boolean = PreferenceRepo(context).pauseSaveOn
    private val isAutoSaveOn: Boolean = PreferenceRepo(context).autoSaveOn

    private val saveTime: Int = if (isAutoSaveOn) {
        context.resources.getIntArray(R.array.value_save_time_array)[PreferenceRepo(context).savePeriod]
    } else {
        0
    }

    private val saveRunnable = {
        callback.onResultSaveControl()
        setSaveHandlerEvent(true)
    }

    /**
     * Пауза срабатывает не только при сворачивании (если закрыли активность например)
     */
    var needSave = true

    fun setSaveHandlerEvent(isStart: Boolean) {
        if (!isAutoSaveOn) return

        if (isStart) {
            saveHandler.postDelayed(saveRunnable, saveTime.toLong())
        } else {
            saveHandler.removeCallbacksAndMessages(null)
        }
    }

    fun onPauseSave(editMode: Boolean) {
        setSaveHandlerEvent(false)

        if (needSave && editMode && isPauseSaveOn) {
            callback.onResultSaveControl()
        } else {
            needSave = true
        }
    }

    interface Callback {
        fun onResultSaveControl()
    }

}