package sgtmelon.scriptum.cleanup.data.room.extension

import kotlinx.coroutines.runBlocking
import sgtmelon.common.test.annotation.RunNone
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Function only for integration tests.
 */
@RunNone
fun IRoomWork.inRoomTest(func: suspend Database.() -> Unit) {
    runBlocking { inRoom(func) }
}

@Deprecated("Use dataSources for communicate with database")
suspend inline fun IRoomWork.inRoom(crossinline func: suspend Database.() -> Unit) {
    roomProvider.openRoom().apply { func() }.close()
}

@Deprecated("Use dataSources for communicate with database")
suspend inline fun <T> IRoomWork.fromRoom(crossinline func: suspend Database.() -> T): T {
    val value: T

    val room = roomProvider.openRoom()
    value = with(room) { func() }
    room.close()

    return value
}