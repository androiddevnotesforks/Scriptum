package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.key.preference.Theme

/**
 * Test for [ThemeConverter].
 */
class ThemeConverterTest : ParentEnumConverterTest<Theme>() {

    override val converter = ThemeConverter()

    override val values: Array<Theme> get() = Theme.values()
}