package sgtmelon.scriptum.app.control

import android.content.Context
import android.os.Handler

import sgtmelon.scriptum.R
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

//    lateinit var noteMenuClick: MenuIntf.Note.NoteMenuClick

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

        when (isStart) {
            true -> saveHandler.postDelayed(saveRunnable, saveTime.toLong())
            false -> saveHandler.removeCallbacks(saveRunnable)
        }
    }

    fun onPauseSave(keyEdit: Boolean) {
        setSaveHandlerEvent(false)

        when (needSave && keyEdit && savePause) {
            true -> onSave()
            false -> needSave = true
        }
    }

    // TODO интерфейс для результата

    private fun onSave() {
//        when (noteMenuClick.onMenuSaveClick(false)) {
//            true -> Toast.makeText(context, context.getString(R.string.toast_note_save_done), Toast.LENGTH_SHORT).show()
//            false -> Toast.makeText(context, context.getString(R.string.toast_note_save_error), Toast.LENGTH_SHORT).show()
//        }
    }

}