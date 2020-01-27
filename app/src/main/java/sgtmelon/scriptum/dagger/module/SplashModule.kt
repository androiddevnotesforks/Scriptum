package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.SplashInteractor
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.vm.SplashViewModel
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel

@Module
class SplashModule {

    @Provides
    @ActivityScope
    fun provideInteractor(preferenceRepo: IPreferenceRepo): ISplashInteractor {
        return SplashInteractor(preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: SplashActivity,
                         interactor: ISplashInteractor): ISplashViewModel {
        return ViewModelProvider(activity).get(SplashViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

}