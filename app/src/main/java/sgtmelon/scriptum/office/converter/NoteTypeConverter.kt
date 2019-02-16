package sgtmelon.scriptum.office.converter

import androidx.room.TypeConverter
import sgtmelon.scriptum.office.annot.key.NoteType

class NoteTypeConverter {

    @TypeConverter
    fun toInt(noteType: NoteType) = noteType.ordinal

    @TypeConverter
    fun toEnum(noteType: Int) = enumValues<NoteType>()[noteType]

}