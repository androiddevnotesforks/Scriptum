package sgtmelon.scriptum.app.screen.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.key.NoteType

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var callback: NoteCallback

    private var id: Long = NoteData.Default.ID
    private var type: NoteType? = null

    fun setupData(bundle: Bundle?) {
        id = bundle?.getLong(NoteData.Intent.ID, NoteData.Default.ID) ?: NoteData.Default.ID
        type = NoteType.values()
                .getOrNull(index = bundle?.getInt(NoteData.Intent.TYPE)
                        ?: NoteData.Default.TYPE)
    }

    fun saveData(bundle: Bundle) {
        bundle.putLong(NoteData.Intent.ID, id)
        bundle.putInt(NoteData.Intent.TYPE, type?.ordinal ?: NoteData.Default.TYPE)
    }

    fun setupFragment(isSave: Boolean) = when (type) {
        NoteType.TEXT -> callback.showTextFragment(id, isSave)
        NoteType.ROLL -> callback.showRollFragment(id, isSave)
        else -> callback.finish()
    }

    fun onPressBack(): Boolean = when (type) {
        NoteType.TEXT -> callback.onPressBackText()
        NoteType.ROLL -> callback.onPressBackRoll()
        else -> false
    }

}