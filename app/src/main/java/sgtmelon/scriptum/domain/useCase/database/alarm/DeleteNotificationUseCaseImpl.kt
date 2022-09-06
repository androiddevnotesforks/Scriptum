package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo

class DeleteNotificationUseCaseImpl(
    private val repository: AlarmRepo
) : DeleteNotificationUseCase {

    override suspend operator fun invoke(noteId: Long) = repository.delete(noteId)
}