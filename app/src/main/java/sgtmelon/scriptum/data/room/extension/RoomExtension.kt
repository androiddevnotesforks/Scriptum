package sgtmelon.scriptum.data.room.extension

import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.domain.model.annotation.test.RunNone

/**
 * Function only for integration tests.
 */
@RunNone
fun IRoomWork.inRoomTest(func: suspend RoomDb.() -> Unit) {
    runBlocking { inRoom(func) }
}

suspend inline fun IRoomWork.inRoom(crossinline func: suspend RoomDb.() -> Unit) {
    roomProvider.openRoom().apply { func() }.close()
}

suspend inline fun <T> IRoomWork.fromRoom(crossinline func: suspend RoomDb.() -> T): T {
    val value: T

    val room = roomProvider.openRoom()
    value = with(room) { func() }
    room.close()

    return value
}