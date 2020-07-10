package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.data.provider.RoomProvider
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

}