package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
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
        alarmDataSource: AlarmDataSource,
        noteDataSource: NoteDataSource
    ): BindRepo {
        return BindRepoImpl(alarmDataSource, noteDataSource)
    }

    @Provides
    @Singleton
    fun provideDevelopRepo(roomProvider: RoomProvider): DevelopRepo = DevelopRepoImpl(roomProvider)

    @Provides
    @Singleton
    fun provideNoteRepo(
        roomProvider: RoomProvider,
        noteConverter: NoteConverter,
        rollConverter: RollConverter
    ): NoteRepo {
        return NoteRepoImpl(roomProvider, noteConverter, rollConverter)
    }

    @Provides
    @Singleton
    fun provideRankRepo(roomProvider: RoomProvider, converter: RankConverter): RankRepo {
        return RankRepoImpl(roomProvider, converter)
    }

    @Provides
    @Singleton
    fun provideBackupRepo(roomProvider: RoomProvider): BackupRepo {
        return BackupRepoImpl(roomProvider)
    }
}