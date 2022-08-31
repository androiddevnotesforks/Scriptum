package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.BindRepoImpl

/**
 * Interface for communicate with [BindRepoImpl]
 */
interface IBindRepo {

    suspend fun unbindNote(id: Long)

    suspend fun getNotificationCount(): Int

}