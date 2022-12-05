package sgtmelon.scriptum.infrastructure.converter.dialog

import androidx.annotation.IdRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.exception.InvalidIdException
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.utils.extensions.record

class RepeatSheetData {

    fun convert(@IdRes itemId: Int): Repeat? {
        val repeat = when (itemId) {
            R.id.item_repeat_0 -> Repeat.MIN_10
            R.id.item_repeat_1 -> Repeat.MIN_30
            R.id.item_repeat_2 -> Repeat.MIN_60
            R.id.item_repeat_3 -> Repeat.MIN_180
            R.id.item_repeat_4 -> Repeat.MIN_1440
            else -> null
        }

        if (repeat == null) {
            InvalidIdException().record()
        }

        return repeat
    }
}