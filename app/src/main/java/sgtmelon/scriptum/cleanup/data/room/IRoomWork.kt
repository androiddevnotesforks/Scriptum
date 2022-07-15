package sgtmelon.scriptum.cleanup.data.room

import sgtmelon.scriptum.cleanup.data.provider.RoomProvider

/**
 * Interface for easy work with Room.
 */
interface IRoomWork {

    // TODO make variable for control room open/close (for not open room every time if it was opened)

    val roomProvider: RoomProvider
}