package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo

class GetNotificationDateListUseCaseImpl(
    private val repository: AlarmRepo
) : GetNotificationDateListUseCase {

    override suspend operator fun invoke(): List<String> = repository.getDateList()
}