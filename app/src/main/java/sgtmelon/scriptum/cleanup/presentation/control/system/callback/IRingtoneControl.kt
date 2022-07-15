package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.domain.model.item.MelodyItem
import sgtmelon.scriptum.cleanup.presentation.control.system.RingtoneControl

/**
 * Interface for [RingtoneControl].
 */
interface IRingtoneControl {
    suspend fun getByType(typeList: List<Int>): List<MelodyItem>
}