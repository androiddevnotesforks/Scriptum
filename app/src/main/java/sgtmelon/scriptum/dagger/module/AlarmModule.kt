package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import javax.inject.Named

@Module
class AlarmModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: AlarmActivity, interactor: IAppInteractor): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }


    @Provides
    @ActivityScope
    fun provideInteractor(activity: AlarmActivity, preferenceRepo: IPreferenceRepo,
                          alarmRepo: IAlarmRepo, noteRepo: INoteRepo): IAlarmInteractor {
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