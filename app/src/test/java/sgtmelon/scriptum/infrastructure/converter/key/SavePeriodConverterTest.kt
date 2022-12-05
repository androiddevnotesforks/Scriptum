package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod

/**
 * Test for [SavePeriodConverter].
 */
class SavePeriodConverterTest : ParentEnumConverterTest<SavePeriod>() {

    override val converter = SavePeriodConverter()

    override val values: Array<SavePeriod> = SavePeriod.values()
}