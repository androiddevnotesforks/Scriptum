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
    private val savePause: Boolean
    private val saveAuto: Boolean

    private var saveTime: Int = 0
    private var noteMenuClick: MenuIntf.Note.NoteMenuClick? = null

    private val saveRunnable = {
        onSave()
        startSaveHandler()
    }

    /**
     * Пауза срабатывает не только при сворачивании (если закрыли активность например)
     */
    private var needSave = true

    init {
        savePause = PrefUtils.getInstance(context).pauseSave
        saveAuto = PrefUtils.getInstance(context).autoSave

        if (saveAuto) {
            val resources = context.resources
            val timeArray = resources.getIntArray(R.array.pref_save_time_value)
            saveTime = timeArray[PrefUtils.getInstance(context).saveTime]
        }
    }

    fun setNoteMenuClick(noteMenuClick: MenuIntf.Note.NoteMenuClick) {
        this.noteMenuClick = noteMenuClick
    }

    fun setNeedSave(needSave: Boolean) {
        this.needSave = needSave
    }

    fun setSaveHandlerEvent(keyEdit: Boolean) {
        if (keyEdit) {
            startSaveHandler()
        } else {
            stopSaveHandler()
        }
    }

    private fun startSaveHandler() {
        if (saveAuto) {
            saveHandler.postDelayed(saveRunnable, saveTime.toLong())
        }
    }

    private fun stopSaveHandler() {
        if (saveAuto) {
            saveHandler.removeCallbacks(saveRunnable)
        }
    }

    private fun onSave() {
        if (noteMenuClick!!.onMenuSaveClick(false, false)) {
            Toast.makeText(context, context.getString(R.string.toast_note_save_done), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.toast_note_save_error), Toast.LENGTH_SHORT).show()
        }
    }

    fun onPauseSave(keyEdit: Boolean) {
        stopSaveHandler()

        if (needSave && keyEdit && savePause) {
            onSave()
        } else {
            needSave = true
        }
    }

}