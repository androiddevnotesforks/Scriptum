package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.PreferencesImpl
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun providePreferenceKeyProvider(resources: Resources): PreferencesKeyProvider {
        return PreferencesKeyProvider(resources)
    }

    @Provides
    fun providePreferenceDefProvider(resources: Resources): PreferencesDefProvider {
        return PreferencesDefProvider(resources)
    }

    @Provides
    @Singleton
    fun providePreferences(
        keyProvider: PreferencesKeyProvider,
        defProvider: PreferencesDefProvider,
        preferences: SharedPreferences
    ): Preferences {
        return PreferencesImpl(keyProvider, defProvider, preferences)
    }
}