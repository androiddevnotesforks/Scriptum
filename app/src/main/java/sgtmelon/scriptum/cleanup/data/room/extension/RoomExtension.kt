package sgtmelon.scriptum.cleanup.data.room.extension

import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.common.test.annotation.RunNone

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