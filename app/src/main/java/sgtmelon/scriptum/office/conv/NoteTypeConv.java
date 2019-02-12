package sgtmelon.scriptum.office.conv;

import androidx.room.TypeConverter;
import sgtmelon.scriptum.office.annot.def.NoteType;

public final class NoteTypeConv { // TODO: 12.02.2019 сделать

    @TypeConverter
    public int fromEnum(NoteType noteType) {
        switch (noteType) {
            case TEXT:
                return 0;
            case ROLL:
            default:
                return 1;
        }
    }

    @TypeConverter
    public NoteType toEnum(int noteType) {
        switch (noteType) {
            case 0:
                return NoteType.TEXT;
            case 1:
            default:
                return NoteType.ROLL;
        }
    }

}
