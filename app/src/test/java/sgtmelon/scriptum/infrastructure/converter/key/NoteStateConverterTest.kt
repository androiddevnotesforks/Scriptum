package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.key.NoteState

/**
 * Test for [NoteStateConverter].
 */
class NoteStateConverterTest : ParentEnumConverterTest<NoteState>() {

    override val converter = NoteStateConverter()

    override val values: Array<NoteState> = NoteState.values()

}