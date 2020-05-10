package sgtmelon.scriptum.data.room

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.data.provider.RoomProvider

/**
 * Interface for easy work with Room.
 */
interface IRoomWork {

    val roomProvider: RoomProvider

    suspend fun inRoom(func: suspend RoomDb.() -> Unit) {
        roomProvider.openRoom()?.apply { func() }?.close()
    }

    suspend fun <T> takeFromRoom(func: suspend RoomDb.() -> T): T? {
        var value: T? = null

        roomProvider.openRoom()?.apply {
            value = func()
        }?.close()

        return value
    }

    /**
     * Function only for integration tests.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun inRoomTest(func: suspend RoomDb.() -> Unit) {
        roomProvider.openRoom()?.apply {
            runBlocking { launch { func() } }
        }?.close()
    }

}