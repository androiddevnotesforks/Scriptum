package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo

class GetNotificationDateListUseCaseImpl(
    private val repository: AlarmRepo
) : GetNotificationDateListUseCase {

    override suspend operator fun invoke(): List<String> = repository.getDateList()
}