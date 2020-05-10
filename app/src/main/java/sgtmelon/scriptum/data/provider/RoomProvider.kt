package sgtmelon.scriptum.data.provider

import android.content.Context
import sgtmelon.scriptum.data.room.RoomDb

/**
 * Provider of [RoomDb] for different repositories.
 */
class RoomProvider(private val context: Context?) {

    fun openRoom() = context?.let { RoomDb[it] }

}