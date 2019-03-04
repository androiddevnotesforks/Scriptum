package sgtmelon.scriptum.app.screen.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData

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

    fun trySave(): Boolean = when (type) {
        NoteType.TEXT -> callback.trySaveTextFragment()
        NoteType.ROLL -> callback.trySaveRollFragment()
        else -> false
    }

}