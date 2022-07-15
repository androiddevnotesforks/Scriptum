package sgtmelon.scriptum.cleanup.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.PrintDevelopActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.AppViewModel

/**
 * Module for [PrintDevelopActivity].
 */
@Module
class PrintModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(
        activity: PrintDevelopActivity,
        interactor: IAppInteractor
    ): IAppViewModel {
        val factory = ViewModelFactory.App(activity, interactor)
        return ViewModelProvider(activity, factory)[AppViewModel::class.java]
    }
}