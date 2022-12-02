package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * ViewModel for [INoteActivity].
 */
class NoteViewModelImpl(
    //    callback: INoteActivity,
    //    private val typeConverter: NoteTypeConverter,
    //    private val colorConverter: ColorConverter,
    private val preferencesRepo: PreferencesRepo
) : /*ParentViewModel<INoteActivity>(callback),*/
    NoteViewModel {

    override val defaultColor: Color get() = preferencesRepo.defaultColor

    //region Cleanup

    //    @RunPrivate var id: Long = Default.ID
    //    @RunPrivate var type: NoteType? = null
    //    @RunPrivate var color: Color = preferencesRepo.defaultColor
    //
    //    override fun onSetup(bundle: Bundle?) {
    //        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
    //
    //        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
    //        val bundleType = typeConverter.toEnum(typeOrdinal)
    //        if (bundleType != null) {
    //            type = bundleType
    //        } else {
    //            callback?.finish()
    //            return
    //        }
    //
    //        val colorOrdinal = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR
    //        val bundleColor = colorConverter.toEnum(colorOrdinal)
    //        if (bundleColor != null) {
    //            color = bundleColor
    //        }
    //
    //        callback?.updateHolder(color)
    //        callback?.setupInsets()
    //    }
    //
    //    override fun onSaveData(bundle: Bundle) = with(bundle) {
    //        putLong(Intent.ID, id)
    //        putInt(Intent.TYPE, type?.ordinal ?: Default.TYPE)
    //        putInt(Intent.COLOR, colorConverter.toInt(color))
    //    }
    //
    //    override fun onSetupFragment(checkCache: Boolean) {
    //        when (type) {
    //            NoteType.TEXT -> callback?.showTextFragment(id, color, checkCache)
    //            NoteType.ROLL -> callback?.showRollFragment(id, color, checkCache)
    //            else -> callback?.finish()
    //        }
    //    }

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

    //endregion

}