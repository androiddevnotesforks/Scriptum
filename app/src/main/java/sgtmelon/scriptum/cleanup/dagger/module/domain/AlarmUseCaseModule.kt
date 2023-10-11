package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationsDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.alarm.TidyUpAlarmUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase

@Module
class AlarmUseCaseModule {

    @Provides
    fun provideGetMelodyListUseCase(dataSource: RingtoneDataSource): GetMelodyListUseCase {
        return GetMelodyListUseCase(dataSource)
    }

    @Provides
    fun provideTidyUpAlarmUseCase(repository: AlarmRepo): TidyUpAlarmUseCase {
        return TidyUpAlarmUseCase(repository)
    }

    @Provides
    fun provideNotifyAlarmUseCase(
        noteRepo: NoteRepo,
        alarmRepo: AlarmRepo
    ): SetNotificationUseCase {
        return SetNotificationUseCase(noteRepo, alarmRepo)
    }

    @Provides
    fun provideDeleteNotificationUseCase(repository: AlarmRepo): DeleteNotificationUseCase {
        return DeleteNotificationUseCase(repository)
    }

    @Provides
    fun provideGetNotificationListUseCase(repository: AlarmRepo): GetNotificationListUseCase {
        return GetNotificationListUseCase(repository)
    }

    @Provides
    fun provideGetNotificationDateListUseCase(
        repository: AlarmRepo
    ): GetNotificationsDateListUseCase {
        return GetNotificationsDateListUseCase(repository)
    }

    @Provides
    fun provideShiftDateOnExistUseCase(repository: AlarmRepo): ShiftDateIfExistUseCase {
        return ShiftDateIfExistUseCase(repository)
    }
}
