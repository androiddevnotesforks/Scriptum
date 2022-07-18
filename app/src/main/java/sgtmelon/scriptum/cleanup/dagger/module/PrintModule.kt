package sgtmelon.scriptum.cleanup.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.PrintDevelopActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.AppViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * Module for [PrintDevelopActivity].
 */
@Module
class PrintModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(
        activity: PrintDevelopActivity,
        preferencesRepo: PreferencesRepo
    ): IAppViewModel {
        val factory = ViewModelFactory.App(activity, preferencesRepo)
        return ViewModelProvider(activity, factory)[AppViewModel::class.java]
    }
}