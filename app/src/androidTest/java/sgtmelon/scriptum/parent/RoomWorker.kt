package sgtmelon.scriptum.parent

import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Class for blocking operations with room.
 */
interface RoomWorker {

    val database: Database

    fun inRoomTest(func: suspend Database.() -> Unit) {
        runBlocking { database.apply { func() } }
    }
}