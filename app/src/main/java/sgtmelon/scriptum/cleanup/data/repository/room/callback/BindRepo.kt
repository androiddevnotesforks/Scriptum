package sgtmelon.scriptum.cleanup.data.repository.room.callback

interface BindRepo {

    suspend fun unbindNote(noteId: Long)

    suspend fun getNotificationsCount(): Int

}