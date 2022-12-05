package sgtmelon.scriptum.domain.useCase.bind

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo

class GetNotificationCountUseCase(private val repository: BindRepo) {

    suspend operator fun invoke(): Int = repository.getNotificationsCount()
}