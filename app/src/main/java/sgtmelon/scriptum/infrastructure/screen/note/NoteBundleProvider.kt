package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.parent.ParentBundleProvider
import sgtmelon.scriptum.infrastructure.utils.extensions.getEnum
import sgtmelon.scriptum.infrastructure.utils.extensions.putEnum

/**
 * Bundle data provider for [NoteActivity] screen.
 */
class NoteBundleProvider(
    private val defaultColor: Color,
    private val typeConverter: NoteTypeConverter,
    private val colorConverter: ColorConverter,
    private val stateConverter: NoteStateConverter
) : ParentBundleProvider {

    private var isEdit: Boolean? = null
    private var noteState: NoteState? = null

    val state: Pair<Boolean, NoteState>?
        get() {
            val isEdit = isEdit ?: return null
            val noteState = noteState ?: return null
            return isEdit to noteState
        }

    private var id: Long? = null
    private var type: NoteType? = null
    private var color: Color? = null

    val data get() = Triple(id, type, color)

    override fun getData(bundle: Bundle?) {
        if (bundle == null) return

        isEdit = bundle.getBoolean(Intent.IS_EDIT, Default.IS_EDIT)
        noteState = bundle.getEnum(Intent.STATE, Default.STATE, stateConverter)

        /** Id may be equals default value, because not created note hasn't id */
        id = bundle.getLong(Intent.ID, Default.ID)
        type = bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter)
        color = bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter) ?: defaultColor
    }

    override fun saveData(outState: Bundle) {
        isEdit?.let { outState.putBoolean(Intent.IS_EDIT, it) }
        noteState?.let { outState.putEnum(Intent.STATE, stateConverter, it) }

        id?.let { outState.putLong(Intent.ID, it) }
        type?.let { outState.putEnum(Intent.TYPE, typeConverter, it) }
        color?.let { outState.putEnum(Intent.COLOR, colorConverter, it) }
    }

    fun updateEdit(isEdit: Boolean) = run { this.isEdit = isEdit }
    fun updateState(noteState: NoteState) = run { this.noteState = noteState }

    fun updateId(id: Long) = run { this.id = id }
    fun updateType(type: NoteType) = run { this.type = type }
    fun updateColor(color: Color) = run { this.color = color }
}