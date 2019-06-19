package sgtmelon.scriptum.room.converter

import androidx.room.TypeConverter
import sgtmelon.scriptum.model.key.NoteType

/**
 * Конвертер из числа в [NoteType]
 *
 * @author SerjantArbuz
 */
class NoteTypeConverter {

    @TypeConverter fun toInt(noteType: NoteType) = noteType.ordinal

    @TypeConverter fun toEnum(noteType: Int) = NoteType.values()[noteType]

}