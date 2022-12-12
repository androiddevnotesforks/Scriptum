package sgtmelon.scriptum.domain.useCase.bind

import sgtmelon.scriptum.data.repository.database.BindRepo

class GetNotificationCountUseCase(private val repository: BindRepo) {

    suspend operator fun invoke(): Int = repository.getNotificationsCount()
}