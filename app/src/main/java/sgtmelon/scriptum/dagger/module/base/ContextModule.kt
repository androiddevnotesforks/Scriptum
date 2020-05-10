package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import javax.inject.Singleton

/**
 * Module for provide applicationContext.
 */
@Module
class ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: ScriptumApplication): Context? = application.applicationContext

    @Provides
    @Singleton
    fun provideResources(context: Context?): Resources? = context?.resources

}