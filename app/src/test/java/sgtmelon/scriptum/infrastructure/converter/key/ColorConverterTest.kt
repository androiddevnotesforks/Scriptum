package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Test for [ColorConverter].
 */
class ColorConverterTest : ParentEnumConverterTest<Color>() {

    override val converter = ColorConverter()

    override val values: Array<Color> get() = Color.values()
}