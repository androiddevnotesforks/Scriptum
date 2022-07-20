package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.Color

class ColorConverter : ParentEnumConverter<Color>() {

    override val values: Array<Color> get() = Color.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, Color::class, ColorConverter::class)
    }
}