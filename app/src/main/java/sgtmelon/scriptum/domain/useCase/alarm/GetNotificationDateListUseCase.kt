package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.data.repository.database.AlarmRepo

class GetNotificationDateListUseCase(private val repository: AlarmRepo) {

    suspend operator fun invoke(): List<String> = repository.getDateList()
}