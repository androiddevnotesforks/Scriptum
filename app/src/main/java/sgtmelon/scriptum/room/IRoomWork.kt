package sgtmelon.scriptum.room

import android.content.Context

/**
 * Интерфейс для упрощённой работы с Room
 *
 * @author SerjantArbuz
 */
interface IRoomWork {

    val context: Context

    fun openRoom() = RoomDb.getInstance(context)

    fun inTheRoom(func: RoomDb.() -> Unit) = openRoom().apply(func).close()

}