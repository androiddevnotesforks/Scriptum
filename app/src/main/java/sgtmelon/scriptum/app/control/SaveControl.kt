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
    private val savePause: Boolean = PrefUtils.getInstance(context).pauseSave
    private val saveAuto: Boolean = PrefUtils.getInstance(context).autoSave

    private var saveTime: Int = 0

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
        if (saveAuto) {
            val resources = context.resources
            val timeArray = resources.getIntArray(R.array.pref_save_time_value)
            saveTime = timeArray[PrefUtils.getInstance(context).saveTime]
        }
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