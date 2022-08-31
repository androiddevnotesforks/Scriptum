package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.repository.room.AlarmRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.BindRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.DevelopRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.NoteRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.RankRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.DevelopRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource

/**
 * Module for provide repo's
 */
@Module
class RepoModule {

    @Provides
    @Singleton
    fun provideAlarmRepo(dataSource: AlarmDataSource, converter: AlarmConverter): AlarmRepo {
        return AlarmRepoImpl(dataSource, converter)
    }

    @Provides
    @Singleton
    fun provideBindRepo(
        noteDataSource: NoteDataSource,
        alarmDataSource: AlarmDataSource
    ): BindRepo {
        return BindRepoImpl(noteDataSource, alarmDataSource)
    }

    @Provides
    @Singleton
    fun provideDevelopRepo(
        noteDataSource: NoteDataSource,
        rollDataSource: RollDataSource,
        rollVisibleDataSource: RollVisibleDataSource,
        rankDataSource: RankDataSource,
        alarmDataSource: AlarmDataSource
    ): DevelopRepo {
        return DevelopRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource,
            rankDataSource, alarmDataSource
        )
    }

    @Provides
    @Singleton
    fun provideNoteRepo(
        noteDataSource: NoteDataSource,
        rollDataSource: RollDataSource,
        rollVisibleDataSource: RollVisibleDataSource,
        rankDataSource: RankDataSource,
        alarmDataSource: AlarmDataSource,
        noteConverter: NoteConverter,
        rollConverter: RollConverter
    ): NoteRepo {
        return NoteRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource, rankDataSource, alarmDataSource,
            noteConverter, rollConverter
        )
    }

    @Provides
    @Singleton
    fun provideRankRepo(
        noteDataSource: NoteDataSource,
        rankDataSource: RankDataSource,
        alarmDataSource: AlarmDataSource,
        converter: RankConverter
    ): RankRepo {
        return RankRepoImpl(noteDataSource, rankDataSource, alarmDataSource, converter)
    }

    @Provides
    @Singleton
    fun provideBackupRepo(
        noteDataSource: NoteDataSource,
        rollDataSource: RollDataSource,
        rollVisibleDataSource: RollVisibleDataSource,
        rankDataSource: RankDataSource,
        alarmDataSource: AlarmDataSource,
    ): BackupRepo {
        return BackupRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource, rankDataSource, alarmDataSource
        )
    }
}