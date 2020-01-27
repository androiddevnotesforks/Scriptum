package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.interactor.notification.NotificationInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

@Module
class NotificationModule {

    @Provides
    @ActivityScope
    fun provideInteractor(iPreferenceRepo: IPreferenceRepo,
                          iAlarmRepo: IAlarmRepo, iBindRepo: IBindRepo,
                          activity: NotificationActivity): INotificationInteractor {
        return NotificationInteractor(iPreferenceRepo, iAlarmRepo, iBindRepo, activity)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: NotificationActivity,
                         iInteractor: INotificationInteractor): INotificationViewModel {
        return ViewModelProvider(activity).get(NotificationViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(iInteractor)
        }
    }

}