package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

/**
 * Test for [SortConverter].
 */
class SortConverterTest : ParentEnumConverterTest<Sort>() {

    override val converter = SortConverter()

    override val values: Array<Sort> get() = Sort.values()
}