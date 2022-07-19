package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.Repeat

class RepeatConverter : ParentEnumConverter<Repeat>() {

    override val values: Array<Repeat> get() = Repeat.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, Repeat::class, RepeatConverter::class)
    }
}