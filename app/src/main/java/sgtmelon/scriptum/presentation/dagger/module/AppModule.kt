package sgtmelon.scriptum.presentation.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Module for [AppActivity].
 */
@Module
class AppModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: AppActivity, interactor: IAppInteractor): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

}