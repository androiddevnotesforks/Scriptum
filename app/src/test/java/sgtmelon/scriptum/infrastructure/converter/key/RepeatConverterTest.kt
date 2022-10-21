package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat

/**
 * Test for [RepeatConverter].
 */
class RepeatConverterTest : ParentEnumConverterTest<Repeat>() {

    override val converter = RepeatConverter()

    override val values: Array<Repeat> get() = Repeat.values()
}