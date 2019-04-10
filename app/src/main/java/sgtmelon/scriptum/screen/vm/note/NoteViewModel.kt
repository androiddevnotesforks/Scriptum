package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.callback.note.NoteCallback
import sgtmelon.scriptum.screen.vm.ParentViewModel

class NoteViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NoteCallback

    private var id: Long = NoteData.Default.ID
    private var type: NoteType? = null

    fun setupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        type = NoteType.values()
                .getOrNull(index = bundle?.getInt(NoteData.Intent.TYPE) ?: NoteData.Default.TYPE)
    }

    fun saveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.TYPE, type?.ordinal ?: NoteData.Default.TYPE)
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