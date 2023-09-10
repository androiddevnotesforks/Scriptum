package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.domain.useCase.alarm.TidyUpAlarmUseCase
import sgtmelon.scriptum.domain.useCase.bind.GetBindNoteListUseCase
import sgtmelon.scriptum.domain.useCase.bind.GetNotificationCountUseCase
import sgtmelon.scriptum.domain.useCase.bind.UnbindNoteUseCase
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import sgtmelon.scriptum.infrastructure.service.EternalServiceLogic
import sgtmelon.scriptum.infrastructure.service.EternalServiceLogicImpl
import javax.inject.Named

@Module
class EternalModule {

    @Provides
    fun provideEternalLogic(
        tidyUpAlarm: TidyUpAlarmUseCase,
        getBindNotes: GetBindNoteListUseCase,
        getNotificationsCount: GetNotificationCountUseCase,
        unbindNote: UnbindNoteUseCase,
        @Named("EternalSystemDelegator") system: SystemDelegatorFactory
    ): EternalServiceLogic {
        return EternalServiceLogicImpl(
            tidyUpAlarm, getBindNotes, getNotificationsCount, unbindNote, system
        )
    }

    @Provides
    @Named("EternalSystemDelegator")
    fun provideEternalSystemDelegator(context: Context): SystemDelegatorFactory {
        return SystemDelegatorFactory(context, lifecycle = null)
    }
}