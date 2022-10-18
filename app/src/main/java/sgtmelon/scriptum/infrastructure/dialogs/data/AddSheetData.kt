package sgtmelon.scriptum.infrastructure.dialogs.data

import androidx.annotation.IdRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.exception.InvalidIdException
import sgtmelon.scriptum.infrastructure.model.key.NoteType
import sgtmelon.scriptum.infrastructure.utils.record

class AddSheetData {

    fun convert(@IdRes itemId: Int): NoteType? {
        val type = when (itemId) {
            R.id.item_add_text -> NoteType.TEXT
            R.id.item_add_roll -> NoteType.ROLL
            else -> null
        }

        if (type == null) {
            InvalidIdException().record()
        }

        return type
    }
}