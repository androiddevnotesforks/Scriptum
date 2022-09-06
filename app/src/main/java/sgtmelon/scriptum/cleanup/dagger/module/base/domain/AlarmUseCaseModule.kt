package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.domain.useCase.database.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.database.alarm.DeleteNotificationUseCaseImpl
import sgtmelon.scriptum.domain.useCase.database.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.database.alarm.GetNotificationDateListUseCaseImpl
import sgtmelon.scriptum.domain.useCase.database.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.database.alarm.GetNotificationListUseCaseImpl
import sgtmelon.scriptum.domain.useCase.database.alarm.GetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.database.alarm.GetNotificationUseCaseImpl
import sgtmelon.scriptum.domain.useCase.database.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.database.alarm.SetNotificationUseCaseImpl

@Module
class AlarmUseCaseModule {

    @Provides
    fun provideNotifyAlarmUseCase(repository: AlarmRepo): SetNotificationUseCase {
        return SetNotificationUseCaseImpl(repository)
    }

    @Provides
    fun provideDeleteNotificationUseCase(repository: AlarmRepo): DeleteNotificationUseCase {
        return DeleteNotificationUseCaseImpl(repository)
    }

    @Provides
    fun provideGetNotificationUseCase(repository: AlarmRepo): GetNotificationUseCase {
        return GetNotificationUseCaseImpl(repository)
    }

    @Provides
    fun provideGetNotificationListUseCase(repository: AlarmRepo): GetNotificationListUseCase {
        return GetNotificationListUseCaseImpl(repository)
    }

    @Provides
    fun provideGetNotificationDateListUseCase(
        repository: AlarmRepo
    ): GetNotificationDateListUseCase {
        return GetNotificationDateListUseCaseImpl(repository)
    }
}
