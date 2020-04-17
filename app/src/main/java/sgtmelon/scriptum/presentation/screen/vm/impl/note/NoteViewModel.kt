package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.data.NoteData.Intent
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.note.INoteViewModel

/**
 * ViewModel for [NoteActivity].
 */
class NoteViewModel(application: Application) : ParentViewModel<INoteActivity>(application),
        INoteViewModel {

    private lateinit var interactor: INoteInteractor

    fun setInteractor(interactor: INoteInteractor) {
        this.interactor = interactor
    }


    @VisibleForTesting var id: Long = Default.ID
    @VisibleForTesting var color: Int = Default.COLOR
    @VisibleForTesting var type: NoteType? = null

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        type = NoteType.values().getOrNull(typeOrdinal)

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