package sgtmelon.scriptum.cleanup.dagger.module.base.data

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.database.dao.RollDao
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao
import sgtmelon.scriptum.infrastructure.database.dataSource.AlarmDataSourceImpl
import sgtmelon.scriptum.infrastructure.database.dataSource.NoteDataSourceImpl
import sgtmelon.scriptum.infrastructure.database.dataSource.RankDataSourceImpl
import sgtmelon.scriptum.infrastructure.database.dataSource.RollDataSourceImpl
import sgtmelon.scriptum.infrastructure.database.dataSource.RollVisibleDataSourceImpl
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.dataSource.PreferencesDataSourceImpl
import sgtmelon.scriptum.infrastructure.system.dataSource.FileDataSourceImpl
import sgtmelon.scriptum.infrastructure.system.dataSource.RingtoneDataSourceImpl

@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun provideAlarmDataSource(dao: AlarmDao): AlarmDataSource = AlarmDataSourceImpl(dao)

    @Singleton
    @Provides
    fun provideNoteDataSource(dao: NoteDao): NoteDataSource = NoteDataSourceImpl(dao)

    @Singleton
    @Provides
    fun provideRankDataSource(dao: RankDao): RankDataSource = RankDataSourceImpl(dao)

    @Singleton
    @Provides
    fun provideRollDataSource(dao: RollDao): RollDataSource = RollDataSourceImpl(dao)

    @Singleton
    @Provides
    fun provideRollVisibleDataSource(dao: RollVisibleDao): RollVisibleDataSource {
        return RollVisibleDataSourceImpl(dao)
    }


    @Singleton
    @Provides
    fun providePreferencesDataSource(preferences: Preferences): PreferencesDataSource {
        return PreferencesDataSourceImpl(preferences)
    }


    @Singleton
    @Provides
    fun provideFileDataSource(context: Context): FileDataSource {
        return FileDataSourceImpl(context)
    }

    @Singleton
    @Provides
    fun provideRingtoneDataSource(context: Context): RingtoneDataSource {
        return RingtoneDataSourceImpl(context)
    }
}