package sgtmelon.scriptum.domain.useCase.alarm

interface DeleteNotificationUseCase {

    suspend operator fun invoke(noteId: Long)
}