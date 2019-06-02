package sgtmelon.scriptum.control

import android.content.Context
import android.os.Handler

import sgtmelon.scriptum.R
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Класс контроля сохранений заметки
 */
class SaveControl(context: Context, private val result: Result) {

    // TODO вынести отсюда PreferenceRepo

    private val saveHandler = Handler()

    private val isPauseSaveOn: Boolean = PreferenceRepo(context).isPauseSaveOn()
    private val isAutoSaveOn: Boolean = PreferenceRepo(context).isAutoSaveOn()

    private val saveTime: Int = if (isAutoSaveOn) {
        context.resources.getIntArray(R.array.pref_save_time_value)[PreferenceRepo(context).getSavePeriod()]
    } else {
        0
    }

    private val saveRunnable = {
        result.onResultSaveControl()
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
            result.onResultSaveControl()
        } else {
            needSave = true
        }
    }

    interface Result {
        fun onResultSaveControl()
    }

}