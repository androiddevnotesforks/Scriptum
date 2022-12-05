package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.converter.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat

class RepeatConverter : ParentEnumConverter<Repeat>() {

    override val values: Array<Repeat> = Repeat.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, Repeat::class, RepeatConverter::class)
    }
}