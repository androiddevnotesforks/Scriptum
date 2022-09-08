package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.data.repository.database.AlarmRepo

class GetNotificationDateListUseCaseImpl(
    private val repository: AlarmRepo
) : GetNotificationDateListUseCase {

    override suspend operator fun invoke(): List<String> = repository.getDateList()
}