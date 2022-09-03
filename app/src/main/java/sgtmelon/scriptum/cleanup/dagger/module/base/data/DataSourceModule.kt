package sgtmelon.scriptum.cleanup.dagger.module.base.data

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.infrastructure.backup.dataSource.BackupDataSourceImpl
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
import sgtmelon.scriptum.infrastructure.system.dataSource.CipherDataSourceImpl
import sgtmelon.scriptum.infrastructure.system.dataSource.FileDataSourceImpl
import sgtmelon.scriptum.infrastructure.system.dataSource.RingtoneDataSourceImpl
import sgtmelon.scriptum.infrastructure.system.dataSource.SummaryDataSourceImpl

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideAlarmDataSource(dao: AlarmDao): AlarmDataSource {
        return AlarmDataSourceImpl(dao)
    }

    @Provides
    @Singleton
    fun provideNoteDataSource(dao: NoteDao): NoteDataSource {
        return NoteDataSourceImpl(dao)
    }

    @Provides
    @Singleton
    fun provideRankDataSource(dao: RankDao): RankDataSource {
        return RankDataSourceImpl(dao)
    }

    @Provides
    @Singleton
    fun provideRollDataSource(dao: RollDao): RollDataSource {
        return RollDataSourceImpl(dao)
    }

    @Provides
    @Singleton
    fun provideRollVisibleDataSource(dao: RollVisibleDao): RollVisibleDataSource {
        return RollVisibleDataSourceImpl(dao)
    }


    @Provides
    @Singleton
    fun providePreferencesDataSource(preferences: Preferences): PreferencesDataSource {
        return PreferencesDataSourceImpl(preferences)
    }


    @Provides
    @Singleton
    fun provideFileDataSource(context: Context): FileDataSource {
        return FileDataSourceImpl(context)
    }

    @Provides
    @Singleton
    fun provideRingtoneDataSource(context: Context): RingtoneDataSource {
        return RingtoneDataSourceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSummaryDataSource(resources: Resources): SummaryDataSource {
        return SummaryDataSourceImpl(resources)
    }

    @Provides
    @Singleton
    fun provideCipherDataSource(): CipherDataSource {
        return CipherDataSourceImpl()
    }


    @Provides
    @Singleton
    fun provideBackupDataSource(context: Context): BackupDataSource {
        return BackupDataSourceImpl(context)
    }
}