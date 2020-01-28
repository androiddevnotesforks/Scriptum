package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel

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

}