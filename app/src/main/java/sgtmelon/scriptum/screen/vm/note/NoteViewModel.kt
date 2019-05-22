package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import android.util.Log
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.callback.note.NoteCallback
import sgtmelon.scriptum.screen.vm.ParentViewModel

class NoteViewModel(application: Application) : ParentViewModel(application), NoteReceiver.Callback {

    lateinit var callback: NoteCallback

    private var id: Long = NoteData.Default.ID
    private var type: NoteType? = null

    fun onSetupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        type = NoteType.values()
                .getOrNull(index = bundle?.getInt(NoteData.Intent.TYPE) ?: NoteData.Default.TYPE)

        Log.i("HERE", "type = ${type?.name}")
    }

    fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.TYPE, type?.ordinal ?: NoteData.Default.TYPE)
    }

    fun onSetupFragment(isSave: Boolean) = when (type) {
        NoteType.TEXT -> callback.showTextFragment(id, isSave)
        NoteType.ROLL -> callback.showRollFragment(id, isSave)
        else -> callback.finish()
    }

    fun onPressBack() = when (type) {
        NoteType.TEXT -> callback.onPressBackText()
        NoteType.ROLL -> callback.onPressBackRoll()
        else -> false
    }

    fun onUpdateNoteId(id: Long) {
        this.id = id
    }

    fun onConvertNote() = when(type) {
        NoteType.TEXT -> {
            type = NoteType.ROLL
            callback.showRollFragment(id, checkCache = true)
        }
        NoteType.ROLL -> {
            type = NoteType.TEXT
            callback.showTextFragment(id, checkCache = true)
        }
        else -> callback.finish()
    }

    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        type?.let { callback.onCancelNoteBind(it) }
    }

}