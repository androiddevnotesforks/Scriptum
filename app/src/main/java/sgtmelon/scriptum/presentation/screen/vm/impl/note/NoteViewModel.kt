package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData.Note.Default
import sgtmelon.scriptum.domain.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [INoteActivity].
 */
class NoteViewModel(
    callback: INoteActivity,
    private val interactor: INoteInteractor
) : ParentViewModel<INoteActivity>(callback),
        INoteViewModel {

    @RunPrivate var id: Long = Default.ID
    @RunPrivate var color: Int = Default.COLOR
    @RunPrivate var type: NoteType? = null

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        type = NoteType.values().getOrNull(typeOrdinal)

        if (color == Default.COLOR) {
            color = interactor.defaultColor
        }

        callback?.updateHolder(color)
        callback?.setupInsets()
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

        callback?.updateHolder(color)
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