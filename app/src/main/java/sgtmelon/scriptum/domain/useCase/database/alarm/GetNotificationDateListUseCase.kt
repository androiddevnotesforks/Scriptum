package sgtmelon.scriptum.domain.useCase.database.alarm

interface GetNotificationDateListUseCase {

    suspend operator fun invoke(): List<String>
}