package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.callback.note.INoteActivity
import sgtmelon.scriptum.screen.callback.note.INoteViewModel
import sgtmelon.scriptum.screen.vm.ParentViewModel

class NoteViewModel(application: Application) : ParentViewModel<INoteActivity>(application),
        INoteViewModel {

    private var id: Long = NoteData.Default.ID
    private var type: NoteType? = null

    override fun onSetupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        type = NoteType.values()
                .getOrNull(index = bundle?.getInt(NoteData.Intent.TYPE) ?: NoteData.Default.TYPE)
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.TYPE, type?.ordinal ?: NoteData.Default.TYPE)
    }

    override fun onSetupFragment(isSave: Boolean) {
        when (type) {
            NoteType.TEXT -> callback?.showTextFragment(id, isSave)
            NoteType.ROLL -> callback?.showRollFragment(id, isSave)
            else -> callback?.finish()
        }
    }

    override fun onPressBack() = when (type) {
        NoteType.TEXT -> callback?.onPressBackText() ?: false
        NoteType.ROLL -> callback?.onPressBackRoll() ?: false
        else -> false
    }

    override fun onUpdateNoteId(id: Long) {
        this.id = id
    }

    override fun onConvertNote() {
        when (type) {
            NoteType.TEXT -> {
                type = NoteType.ROLL
                callback?.showRollFragment(id, checkCache = true)
            }
            NoteType.ROLL -> {
                type = NoteType.TEXT
                callback?.showTextFragment(id, checkCache = true)
            }
            else -> callback?.finish()
        }
    }

    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        type?.let { callback?.onCancelNoteBind(it) }
    }

}