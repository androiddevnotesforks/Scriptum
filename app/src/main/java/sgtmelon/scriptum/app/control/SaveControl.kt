package sgtmelon.scriptum.app.control

import android.content.Context
import android.os.Handler

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.utils.Preference

/**
 * Класс контроля сохранений заметки
 */
class SaveControl(context: Context, private val result: Result) {

    // TODO вынести отсюда Preference

    private val saveHandler = Handler()
    private val savePause: Boolean = Preference(context).pauseSave
    private val saveAuto: Boolean = Preference(context).autoSave

    private val saveTime: Int = when (saveAuto) {
        true -> context.resources.getIntArray(R.array.pref_save_time_value)[Preference(context).saveTime]
        false -> 0
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
        if (!saveAuto) return

        when (isStart) {
            true -> saveHandler.postDelayed(saveRunnable, saveTime.toLong())
            false -> saveHandler.removeCallbacks(saveRunnable)
        }
    }

    fun onPauseSave(keyEdit: Boolean) {
        setSaveHandlerEvent(false)

        when (needSave && keyEdit && savePause) {
            true -> result.onResultSaveControl()
            false -> needSave = true
        }
    }

    interface Result {
        fun onResultSaveControl()
    }

}