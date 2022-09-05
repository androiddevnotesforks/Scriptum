package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

class GetNotificationDateListUseCaseImpl(
    private val dataSource: AlarmDataSource
) : GetNotificationDateListUseCase {

    override suspend operator fun invoke(): List<String> = dataSource.getDateList()
}