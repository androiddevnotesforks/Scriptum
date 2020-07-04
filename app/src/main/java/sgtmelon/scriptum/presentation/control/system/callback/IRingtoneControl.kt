package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.presentation.control.system.RingtoneControl

/**
 * Interface for [RingtoneControl].
 */
interface IRingtoneControl {
    suspend fun getByType(typeList: List<Int>): List<MelodyItem>
}