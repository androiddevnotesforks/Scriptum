package sgtmelon.scriptum.domain.useCase.alarm

interface GetNotificationDateListUseCase {

    suspend operator fun invoke(): List<String>
}