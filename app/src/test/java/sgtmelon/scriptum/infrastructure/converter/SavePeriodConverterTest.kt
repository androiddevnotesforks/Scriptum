package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.SavePeriod

/**
 * Test for [SavePeriodConverter].
 */
class SavePeriodConverterTest : ParentEnumConverterTest<SavePeriod>() {

    override val converter = SavePeriodConverter()

    override val values: Array<SavePeriod> get() = SavePeriod.values()
}