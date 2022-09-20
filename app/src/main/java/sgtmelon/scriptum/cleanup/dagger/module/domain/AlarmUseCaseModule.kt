package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase

@Module
class AlarmUseCaseModule {

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
    fun provideGetNotificationUseCase(repository: AlarmRepo): GetNotificationUseCase {
        return GetNotificationUseCase(repository)
    }

    @Provides
    fun provideGetNotificationListUseCase(repository: AlarmRepo): GetNotificationListUseCase {
        return GetNotificationListUseCase(repository)
    }

    @Provides
    fun provideGetNotificationDateListUseCase(
        repository: AlarmRepo
    ): GetNotificationDateListUseCase {
        return GetNotificationDateListUseCase(repository)
    }

    @Provides
    fun provideShiftDateOnExistUseCase(repository: AlarmRepo): ShiftDateIfExistUseCase {
        return ShiftDateIfExistUseCase(repository)
    }
}
