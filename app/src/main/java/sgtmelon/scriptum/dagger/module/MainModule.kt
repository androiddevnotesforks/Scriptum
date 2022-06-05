package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.other.ViewModelFactory
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Module for [MainActivity].
 */
@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: MainActivity, interactor: IAppInteractor): IAppViewModel {
        val factory = ViewModelFactory.App(activity, interactor)
        return ViewModelProvider(activity, factory)[AppViewModel::class.java]
    }
}