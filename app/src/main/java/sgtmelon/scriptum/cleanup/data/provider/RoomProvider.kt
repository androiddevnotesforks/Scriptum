package sgtmelon.scriptum.cleanup.data.provider

import android.content.Context
import sgtmelon.scriptum.cleanup.data.room.Database

/**
 * Provider of [Database] for different repositories.
 */
class RoomProvider(private val context: Context) {

    fun openRoom() = Database[context]
}