package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Module for [AlarmActivity].
 */
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

}