package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.ParentNoteBundleProvider
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteStateConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.getEnum
import sgtmelon.scriptum.infrastructure.utils.extensions.putEnum

/**
 * Bundle data provider for [NoteActivity] screen.
 */
class NoteBundleProvider(
    private val defaultColor: Color,
    private val typeConverter: NoteTypeConverter,
    private val colorConverter: ColorConverter,
    stateConverter: NoteStateConverter
) : ParentNoteBundleProvider(stateConverter) {

    private var id: Long? = null
    private var type: NoteType? = null
    private var color: Color? = null

    val data get() = Triple(id, type, color)

    override fun getData(bundle: Bundle?) {
        super.getData(bundle)

        if (bundle == null) return

        id = bundle.getLong(Intent.ID, Default.ID).takeIf { it != Default.ID }
        type = bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter)
        color = bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter) ?: defaultColor
    }

    override fun saveData(outState: Bundle) {
        super.saveData(outState)

        id?.let { outState.putLong(Intent.ID, it) }
        type?.let { outState.putEnum(Intent.TYPE, typeConverter, it) }
        color?.let { outState.putEnum(Intent.COLOR, colorConverter, it) }
    }

    fun updateId(id: Long) = run { this.id = id }
    fun updateType(type: NoteType) = run { this.type = type }
    fun updateColor(color: Color) = run { this.color = color }
}