package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.presentation.screen.ui.impl.ScriptumApplication
import javax.inject.Singleton

/**
 * Module for provide applicationContext.
 */
@Module
class ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: ScriptumApplication): Context = application.applicationContext

}