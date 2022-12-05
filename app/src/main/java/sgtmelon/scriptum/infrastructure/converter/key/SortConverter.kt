package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.converter.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

class SortConverter : ParentEnumConverter<Sort>() {

    override val values: Array<Sort> = Sort.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, Sort::class, SortConverter::class)
    }
}