package sgtmelon.scriptum.cleanup.dagger.module.base.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.dataSource.PreferencesDataSourceImpl

@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun providePreferencesDataSource(preferences: Preferences): PreferencesDataSource {
        return PreferencesDataSourceImpl(preferences)
    }
}