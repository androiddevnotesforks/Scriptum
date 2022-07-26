package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note

import android.os.Bundle
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note.Default
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * ViewModel for [INoteActivity].
 */
class NoteViewModel(
    callback: INoteActivity,
    private val typeConverter: NoteTypeConverter,
    private val colorConverter: ColorConverter,
    preferencesRepo: PreferencesRepo
) : ParentViewModel<INoteActivity>(callback),
        INoteViewModel {

    @RunPrivate var id: Long = Default.ID
    @RunPrivate var type: NoteType? = null
    @RunPrivate var color: Color = preferencesRepo.defaultColor

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID

        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        val bundleType = typeConverter.toEnum(typeOrdinal)
        if (bundleType != null) {
            type = bundleType
        } else {
            IllegalAccessException("Passed wrong type via bundle: $typeOrdinal").record()
            callback?.finish()
            return
        }

        val colorOrdinal = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR
        val bundleColor = colorConverter.toEnum(colorOrdinal)
        if (bundleColor != null) {
            color = bundleColor
        }

        callback?.updateHolder(color)
        callback?.setupInsets()
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(Intent.ID, id)
        putInt(Intent.TYPE, type?.ordinal ?: Default.TYPE)
        putInt(Intent.COLOR, colorConverter.toInt(color))
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

    override fun onUpdateNoteColor(color: Color) {
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