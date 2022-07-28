package sgtmelon.scriptum.infrastructure.provider

import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interface for [RingtoneControl].
 */
interface IRingtoneControl {
    suspend fun getByType(typeList: List<Int>): List<MelodyItem>
}