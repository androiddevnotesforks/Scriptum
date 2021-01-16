package sgtmelon.scriptum.data.room

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.domain.model.annotation.test.RunNone

/**
 * Interface for easy work with Room.
 */
interface IRoomWork {

    // TODO make variable for control room open/close (for not open room every time if it was opened)

    val roomProvider: RoomProvider

    suspend fun inRoom(func: suspend RoomDb.() -> Unit = {}) {
        roomProvider.openRoom().apply { func() }.close()
    }

    suspend fun <T> takeFromRoom(func: suspend RoomDb.() -> T): T {
        var value: T

        roomProvider.openRoom().apply {
            value = func()
        }.close()

        return value
    }

    /**
     * Function only for integration tests.
     */
    @RunNone
    fun inRoomTest(func: suspend RoomDb.() -> Unit) {
        roomProvider.openRoom().apply {
            runBlocking { launch { func() } }
        }.close()
    }

}