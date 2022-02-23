package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Module for [NotificationActivity].
 */
@Module
class NotificationModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: NotificationActivity, interactor: IAppInteractor): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }
}