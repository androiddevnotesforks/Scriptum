package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

@Module
class AlarmModule {

    @Provides
    @ActivityScope
    fun provideInteractor(preferenceRepo: IPreferenceRepo, alarmRepo: IAlarmRepo,
                          noteRepo: INoteRepo, activity: AlarmActivity): IAlarmInteractor {
        return AlarmInteractor(preferenceRepo, alarmRepo, noteRepo, activity)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: AlarmActivity, interactor: IAlarmInteractor,
                         signalInteractor: ISignalInteractor,
                         bindInteractor: IBindInteractor): IAlarmViewModel {
        return ViewModelProvider(activity).get(AlarmViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor, signalInteractor, bindInteractor)
        }
    }

}