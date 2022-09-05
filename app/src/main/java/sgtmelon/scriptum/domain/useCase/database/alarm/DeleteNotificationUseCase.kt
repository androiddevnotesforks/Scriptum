package sgtmelon.scriptum.domain.useCase.database.alarm

interface DeleteNotificationUseCase {

    suspend operator fun invoke(noteId: Long)
}