package sgtmelon.scriptum.room

import android.content.Context
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Interface for easy work with Room
 */
interface IRoomWork {

    val context: Context

    fun openRoom() = RoomDb[context]

    fun inRoom(func: suspend RoomDb.() -> Unit) {
        openRoom().apply {
            runBlocking { launch { func() } }
        }.close()
    }

}