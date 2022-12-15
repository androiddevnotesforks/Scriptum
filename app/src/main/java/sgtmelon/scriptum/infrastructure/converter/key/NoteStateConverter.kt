package sgtmelon.scriptum.infrastructure.converter.key

import sgtmelon.scriptum.infrastructure.model.exception.converter.EnumConverterException
import sgtmelon.scriptum.infrastructure.model.key.NoteState

class NoteStateConverter : ParentEnumConverter<NoteState>() {

    override val values: Array<NoteState> = NoteState.values()

    override fun getOrdinalException(ordinal: Int): EnumConverterException {
        return EnumConverterException(ordinal, NoteState::class, NoteStateConverter::class)
    }
}