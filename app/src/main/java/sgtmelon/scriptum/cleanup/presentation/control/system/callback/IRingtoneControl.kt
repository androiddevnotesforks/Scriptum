package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interface for [RingtoneControl].
 */
interface IRingtoneControl {
    suspend fun getByType(typeList: List<Int>): List<MelodyItem>
}