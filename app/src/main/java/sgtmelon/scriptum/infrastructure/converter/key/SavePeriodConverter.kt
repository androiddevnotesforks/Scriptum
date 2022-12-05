package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.converter.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod

class SavePeriodConverter : ParentEnumConverter<SavePeriod>() {

    override val values: Array<SavePeriod> = SavePeriod.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, SavePeriod::class, SavePeriodConverter::class)
    }
}