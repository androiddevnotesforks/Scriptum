package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Test for [SortConverter].
 */
class SortConverterTest : ParentEnumConverterTest<Sort>() {

    override val converter = SortConverter()

    override val values: Array<Sort> get() = Sort.values()
}