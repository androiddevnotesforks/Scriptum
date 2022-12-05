package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Test for [NoteTypeConverter].
 */
class NoteTypeConverterTest : ParentEnumConverterTest<NoteType>() {

    override val converter = NoteTypeConverter()

    override val values: Array<NoteType> = NoteType.values()

}