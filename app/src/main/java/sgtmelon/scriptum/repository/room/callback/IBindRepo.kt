package sgtmelon.scriptum.repository.room.callback

import sgtmelon.scriptum.repository.room.BindRepo

/**
 * Interface for communicate with [BindRepo]
 */
interface IBindRepo {

    suspend fun unbindNote(id: Long): Boolean

    suspend fun getNotificationCount(): Int

}