package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
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
    private val typeConverter: NoteTypeConverter,
    private val colorConverter: ColorConverter
) {

    private var _id: Long? = null
    val id: Long? get() = _id

    private var _type: NoteType? = null
    val type get() = _type

    private var _color: Color? = null
    val color get() = _color

    val values get() = Triple(id, type, color)

    fun getData(bundle: Bundle?, defaultColor: Color) {
        if (bundle == null) return

        _id = bundle.getLong(Intent.ID, Default.ID).takeIf { it != Default.ID }
        _type = bundle.getEnum(Intent.TYPE, Default.TYPE, typeConverter)
        _color = bundle.getEnum(Intent.COLOR, Default.COLOR, colorConverter) ?: defaultColor
    }

    fun saveData(outState: Bundle) {
        id?.let { outState.putLong(Intent.ID, it) }
        type?.let { outState.putEnum(Intent.TYPE, typeConverter, it) }
        color?.let { outState.putEnum(Intent.COLOR, colorConverter, it) }
    }

    fun updateId(id: Long) {
        _id = id
    }

    fun updateType(type: NoteType) {
        _type = type
    }

    fun updateColor(color: Color) {
        _color = color
    }
}