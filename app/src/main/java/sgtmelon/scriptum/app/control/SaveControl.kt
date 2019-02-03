package sgtmelon.scriptum.app.control

import android.content.Context
import android.os.Handler
import android.widget.Toast

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.intf.MenuIntf
import sgtmelon.scriptum.office.utils.PrefUtils

/**
 * Класс контроля сохранений заметки
 */
class SaveControl(private val context: Context) {

    private val saveHandler = Handler()
    private val savePause: Boolean = PrefUtils(context).pauseSave
    private val saveAuto: Boolean = PrefUtils(context).autoSave

    private val saveTime: Int

    private val saveRunnable = {
        onSave()
        setSaveHandlerEvent(true)
    }

    var noteMenuClick: MenuIntf.Note.NoteMenuClick? = null

    /**
     * Пауза срабатывает не только при сворачивании (если закрыли активность например)
     */
    var needSave = true

    init {
        saveTime = if (saveAuto) {
            val timeArray = context.resources.getIntArray(R.array.pref_save_time_value)
            timeArray[PrefUtils(context).saveTime]
        } else 0
    }

    fun setSaveHandlerEvent(isStart: Boolean) {
        if (!saveAuto) return

        if (isStart) {
            saveHandler.postDelayed(saveRunnable, saveTime.toLong())
        } else {
            saveHandler.removeCallbacks(saveRunnable)
        }
    }

    fun onPauseSave(keyEdit: Boolean) {
        setSaveHandlerEvent(false)

        if (needSave && keyEdit && savePause) {
            onSave()
        } else {
            needSave = true
        }
    }

    private fun onSave() {
        if (noteMenuClick!!.onMenuSaveClick(false, false)) {
            Toast.makeText(context, context.getString(R.string.toast_note_save_done), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.toast_note_save_error), Toast.LENGTH_SHORT).show()
        }
    }

}