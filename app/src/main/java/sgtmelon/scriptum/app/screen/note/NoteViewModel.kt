package sgtmelon.scriptum.app.screen.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.office.annot.key.NoteType

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var callback: NoteCallback

    private var id: Long = UNDEFINED_ID
    private var type: NoteType? = null

    fun setupData(bundle: Bundle?) {
        id = bundle?.getLong(NoteActivity.NOTE_ID) ?: UNDEFINED_ID
        type = NoteType.values()
                .getOrNull(bundle?.getInt(NoteActivity.NOTE_TYPE) ?: UNDEFINED_TYPE)
    }

    fun saveData(bundle: Bundle) {
        bundle.putLong(NoteActivity.NOTE_ID, id)
        bundle.putInt(NoteActivity.NOTE_TYPE, type?.ordinal ?: UNDEFINED_TYPE)
    }

    fun setupFragment(isSave: Boolean) = when (type) {
        NoteType.TEXT -> callback.showTextFragment(id, isSave)
        NoteType.ROLL -> callback.showRollFragment(id, isSave)
        else -> callback.finish()
    }

    companion object {
        const val UNDEFINED_ID = -1L
        private const val UNDEFINED_TYPE = -1
    }

}