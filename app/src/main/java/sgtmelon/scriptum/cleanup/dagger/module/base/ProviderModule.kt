package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.infrastructure.preferences.PreferenceProvider
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import javax.inject.Singleton

/**
 * Module for provide providers classes.
 */
@Module
class ProviderModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
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

    @Provides
    @Singleton
    fun provideSummaryProvider(resources: Resources): SummaryProvider {
        return SummaryProvider(resources)
    }

}