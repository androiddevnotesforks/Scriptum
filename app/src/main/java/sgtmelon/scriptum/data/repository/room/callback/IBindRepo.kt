package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.BindRepo

/**
 * Interface for communicate with [BindRepo]
 */
interface IBindRepo {

    suspend fun unbindNote(id: Long): Unit

    suspend fun getNotificationCount(): Int

}