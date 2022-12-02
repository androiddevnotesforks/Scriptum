package sgtmelon.scriptum.infrastructure.converter.key

import androidx.room.TypeConverter
import sgtmelon.scriptum.infrastructure.model.exception.converter.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class NoteTypeConverter : ParentEnumConverter<NoteType>() {

    override val values: Array<NoteType> = NoteType.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, NoteType::class, NoteTypeConverter::class)
    }

    @TypeConverter override fun toInt(value: NoteType) = super.toInt(value)

    @TypeConverter override fun toEnum(value: Int): NoteType? = super.toEnum(value)

}