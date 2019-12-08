package sgtmelon.scriptum.room.converter.type

import androidx.room.TypeConverter
import sgtmelon.scriptum.model.key.NoteType

/**
 * Converter from number to [NoteType] and vice versa
 */
class NoteTypeConverter {

    @TypeConverter fun toInt(noteType: NoteType) = noteType.ordinal

    @TypeConverter fun toEnum(noteType: Int) = NoteType.values()[noteType]

}