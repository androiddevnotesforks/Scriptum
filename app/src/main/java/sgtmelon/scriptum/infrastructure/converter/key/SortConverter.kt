package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.Sort

class SortConverter : ParentEnumConverter<Sort>() {

    override val values: Array<Sort> get() = Sort.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, Sort::class, SortConverter::class)
    }
}