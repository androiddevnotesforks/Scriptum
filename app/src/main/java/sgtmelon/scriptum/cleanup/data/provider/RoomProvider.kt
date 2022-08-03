package sgtmelon.scriptum.cleanup.data.provider

import android.content.Context
import sgtmelon.scriptum.cleanup.data.room.RoomDb

/**
 * Provider of [RoomDb] for different repositories.
 */
class RoomProvider(private val context: Context) {

    fun openRoom() = RoomDb[context]
}