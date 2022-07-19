package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Test for [ThemeConverter].
 */
class ThemeConverterTest : ParentEnumConverterTest<Theme>() {

    override val converter = ThemeConverter()

    override val values: Array<Theme> get() = Theme.values()
}