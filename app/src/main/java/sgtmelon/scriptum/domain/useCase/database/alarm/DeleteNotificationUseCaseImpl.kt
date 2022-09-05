package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

class DeleteNotificationUseCaseImpl(
    private val dataSource: AlarmDataSource,
) : DeleteNotificationUseCase {

    override suspend operator fun invoke(noteId: Long) = dataSource.delete(noteId)
}