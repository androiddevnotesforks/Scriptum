package sgtmelon.scriptum.cleanup.dagger.module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.NoteRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.RankRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource

// TODO remove it, after migration all repos to another package (from 'cleanup' to 'data')
@Module
class RepoModule {

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
        alarmDataSource: AlarmDataSource
    ): BackupRepo {
        return BackupRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource, rankDataSource, alarmDataSource
        )
    }
}