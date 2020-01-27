package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.model.data.NoteData.Default
import sgtmelon.scriptum.model.data.NoteData.Intent
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel

/**
 * ViewModel for [NoteActivity].
 */
class NoteViewModel(application: Application) : ParentViewModel<INoteActivity>(application),
        INoteViewModel {

    private lateinit var interactor: INoteInteractor

    fun setInteractor(interactor: INoteInteractor) {
        this.interactor = interactor
    }


    private var id: Long = Default.ID
    private var color: Int = Default.COLOR
    private var type: NoteType? = null

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR
        type = NoteType.values().getOrNull(index = bundle?.getInt(Intent.TYPE) ?: Default.TYPE)

        if (color == Default.COLOR) {
            color = interactor.defaultColor
        }

        callback?.updateHolder(interactor.theme, color)
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(Intent.ID, id)
        putInt(Intent.COLOR, color)
        putInt(Intent.TYPE, type?.ordinal ?: Default.TYPE)
    }

    override fun onSetupFragment(checkCache: Boolean) {
        when (type) {
            NoteType.TEXT -> callback?.showTextFragment(id, color, checkCache)
            NoteType.ROLL -> callback?.showRollFragment(id, color, checkCache)
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

    override fun onUpdateNoteColor(color: Int) {
        this.color = color

        callback?.updateHolder(interactor.theme, color)
    }

    override fun onConvertNote() {
        when (type) {
            NoteType.TEXT -> {
                type = NoteType.ROLL
                callback?.showRollFragment(id, color, checkCache = true)
            }
            NoteType.ROLL -> {
                type = NoteType.TEXT
                callback?.showTextFragment(id, color, checkCache = true)
            }
            else -> callback?.finish()
        }
    }

}