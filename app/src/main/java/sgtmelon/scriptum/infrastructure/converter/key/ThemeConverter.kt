package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.converter.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme

class ThemeConverter : ParentEnumConverter<Theme>() {

    override val values: Array<Theme> = Theme.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, Theme::class, ThemeConverter::class)
    }
}