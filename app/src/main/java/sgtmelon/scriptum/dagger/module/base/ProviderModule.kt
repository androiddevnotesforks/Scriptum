package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import javax.inject.Singleton

/**
 * Module for provide providers classes.
 */
@Module
class ProviderModule {

    // TODO remove singleton
    @Provides
    @Singleton
    fun provideSummaryProvider(resources: Resources): SummaryProvider {
        return SummaryProvider(resources)
    }

    @Provides
    @Singleton
    fun providePreferenceKeyProvider(resources: Resources): PreferenceProvider.Key {
        return PreferenceProvider.Key(resources)
    }

    @Provides
    @Singleton
    fun providePreferenceDefProvider(resources: Resources): PreferenceProvider.Def {
        return PreferenceProvider.Def(resources)
    }

    @Provides
    @Singleton
    fun provideRoomProvider(context: Context): RoomProvider = RoomProvider(context)

}