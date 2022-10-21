package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme

// TODO move into key package (don't forget about tests)
class ThemeConverter : ParentEnumConverter<Theme>() {

    override val values: Array<Theme> get() = Theme.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, Theme::class, ThemeConverter::class)
    }
}