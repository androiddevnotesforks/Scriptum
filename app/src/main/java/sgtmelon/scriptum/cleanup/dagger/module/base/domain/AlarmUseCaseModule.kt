package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCaseImpl
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCaseImpl
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCaseImpl
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationUseCaseImpl
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCaseImpl
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCaseImpl

@Module
class AlarmUseCaseModule {

    @Provides
    fun provideNotifyAlarmUseCase(
        noteRepo: NoteRepo,
        alarmRepo: AlarmRepo
    ): SetNotificationUseCase {
        return SetNotificationUseCaseImpl(noteRepo, alarmRepo)
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

    @Provides
    fun provideShiftDateOnExistUseCase(repository: AlarmRepo): ShiftDateIfExistUseCase {
        return ShiftDateIfExistUseCaseImpl(repository)
    }
}
