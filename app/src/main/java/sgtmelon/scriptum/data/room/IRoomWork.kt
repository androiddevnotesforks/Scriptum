package sgtmelon.scriptum.data.room

import android.content.Context
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Interface for easy work with Room
 */
interface IRoomWork {

    val context: Context

    fun openRoom() = RoomDb[context]

    suspend fun inRoom(func: suspend RoomDb.() -> Unit) = openRoom().apply { func() }.close()

    /**
     * Function only integration tests.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun inRoomTest(func: suspend RoomDb.() -> Unit) {
        openRoom().apply {
            runBlocking { launch { func() } }
        }.close()
    }

}