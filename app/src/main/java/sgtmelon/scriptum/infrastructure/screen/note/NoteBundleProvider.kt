package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

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

    fun getData(bundle: Bundle?, defaultColor: Color) {
        if (bundle == null) return

        _id = bundle.getLong(Intent.ID, Default.ID).takeIf { it != Default.ID }

        val typeOrdinal = bundle.getInt(Intent.TYPE, Default.TYPE).takeIf { it != Default.TYPE }
        if (typeOrdinal != null) {
            _type = typeConverter.toEnum(typeOrdinal)
        }

        val colorOrdinal = bundle.getInt(Intent.COLOR, Default.COLOR).takeIf { it != Default.COLOR }
        if (colorOrdinal != null) {
            _color = colorConverter.toEnum(colorOrdinal) ?: defaultColor
        } else {
            _color = defaultColor
        }
    }

    fun saveData(outState: Bundle) {
        val id = id
        if (id != null) {
            outState.putLong(Intent.ID, id)
        }

        val type = type
        if (type != null) {
            outState.putInt(Intent.TYPE, typeConverter.toInt(type))
        }

        val color = color
        if (color != null) {
            outState.putInt(Intent.COLOR, colorConverter.toInt(color))
        }
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