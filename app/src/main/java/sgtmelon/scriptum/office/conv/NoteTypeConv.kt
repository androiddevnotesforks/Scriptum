package sgtmelon.scriptum.office.conv

import androidx.room.TypeConverter
import sgtmelon.scriptum.office.annot.def.NoteType

class NoteTypeConv {

    @TypeConverter
    fun toInt(noteType: NoteType) = when (noteType) {
        NoteType.TEXT -> 0
        else -> 1
    }

    @TypeConverter
    fun toEnum(noteType: Int) = when (noteType) {
        0 -> NoteType.TEXT
        else -> NoteType.ROLL
    }

}