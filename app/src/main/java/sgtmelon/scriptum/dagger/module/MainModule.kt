package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.interactor.main.MainInteractor
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.screen.vm.main.MainViewModel

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: MainActivity, interactor: IAppInteractor): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun provideInteractor(activity: MainActivity, alarmRepo: IAlarmRepo): IMainInteractor {
        return MainInteractor(alarmRepo, activity)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: MainActivity, interactor: IMainInteractor,
                         bindInteractor: IBindInteractor): IMainViewModel {
        return ViewModelProvider(activity).get(MainViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor, bindInteractor)
        }
    }

}