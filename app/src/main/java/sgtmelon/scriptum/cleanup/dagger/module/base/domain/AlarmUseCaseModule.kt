package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
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
    fun provideNotifyAlarmUseCase(
        dataSource: AlarmDataSource,
        converter: AlarmConverter
    ): SetNotificationUseCase {
        return SetNotificationUseCaseImpl(dataSource, converter)
    }

    @Provides
    fun provideDeleteNotificationUseCase(dataSource: AlarmDataSource): DeleteNotificationUseCase {
        return DeleteNotificationUseCaseImpl(dataSource)
    }

    @Provides
    fun provideGetNotificationUseCase(dataSource: AlarmDataSource): GetNotificationUseCase {
        return GetNotificationUseCaseImpl(dataSource)
    }

    @Provides
    fun provideGetNotificationListUseCase(dataSource: AlarmDataSource): GetNotificationListUseCase {
        return GetNotificationListUseCaseImpl(dataSource)
    }

    @Provides
    fun provideGetNotificationDateListUseCase(
        dataSource: AlarmDataSource
    ): GetNotificationDateListUseCase {
        return GetNotificationDateListUseCaseImpl(dataSource)
    }
}
