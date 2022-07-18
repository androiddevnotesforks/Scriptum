package sgtmelon.scriptum.cleanup.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.AppViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * Module for [NotificationActivity].
 */
@Module
class NotificationModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: NotificationActivity, preferencesRepo: PreferencesRepo): IAppViewModel {
        val factory = ViewModelFactory.App(activity, preferencesRepo)
        return ViewModelProvider(activity, factory)[AppViewModel::class.java]
    }
}