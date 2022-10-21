package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod

class SavePeriodConverter : ParentEnumConverter<SavePeriod>() {

    override val values: Array<SavePeriod> get() = SavePeriod.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, SavePeriod::class, SavePeriodConverter::class)
    }
}