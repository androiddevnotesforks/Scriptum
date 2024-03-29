package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.infrastructure.screen.ScriptumApplication
import javax.inject.Singleton

@Module
class ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: ScriptumApplication): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideResources(context: Context): Resources = context.resources
}