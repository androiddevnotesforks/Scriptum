package sgtmelon.scriptum.cleanup.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.AppViewModel

/**
 * Module for [AlarmActivity].
 */
@Module
class AlarmModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: AlarmActivity, interactor: IAppInteractor): IAppViewModel {
        val factory = ViewModelFactory.App(activity, interactor)
        return ViewModelProvider(activity, factory)[AppViewModel::class.java]
    }
}