package sgtmelon.scriptum.data.repository.database

interface BindRepo {

    suspend fun unbindNote(noteId: Long)

    suspend fun getNotificationsCount(): Int

}