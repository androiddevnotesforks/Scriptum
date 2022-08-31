package sgtmelon.scriptum.cleanup.data.provider

import android.content.Context
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Provider of [Database] for different repositories.
 */
@Deprecated("Use di for provide room database")
class RoomProvider(private val context: Context) {

    fun openRoom() = Database[context]
}