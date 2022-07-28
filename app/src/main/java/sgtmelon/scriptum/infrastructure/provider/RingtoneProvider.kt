package sgtmelon.scriptum.infrastructure.provider

import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interface for [RingtoneProviderImpl].
 */
interface RingtoneProvider {
    suspend fun getByType(typeList: List<Int>): List<MelodyItem>
}