package sgtmelon.scriptum.room

import android.content.Context

/**
 * Interface for easy work with Room
 *
 * @author SerjantArbuz
 */
interface IRoomWork {

    val context: Context

    fun openRoom() = RoomDb.getInstance(context)

    fun inRoom(func: RoomDb.() -> Unit) = openRoom().apply(func).close()

}