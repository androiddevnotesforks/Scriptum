package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.repository.room.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.DevelopRepo
import sgtmelon.scriptum.cleanup.data.repository.room.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.RankRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter

/**
 * Module for provide repo's
 */
@Module
class RepoModule {

    // TODO #PREF move into another place



    @Provides
    @Singleton
    fun provideAlarmRepo(roomProvider: RoomProvider, converter: AlarmConverter): IAlarmRepo {
        return AlarmRepo(roomProvider, converter)
    }

    @Provides
    @Singleton
    fun provideBindRepo(roomProvider: RoomProvider): IBindRepo = BindRepo(roomProvider)

    @Provides
    @Singleton
    fun provideDevelopRepo(roomProvider: RoomProvider): IDevelopRepo = DevelopRepo(roomProvider)

    @Provides
    @Singleton
    fun provideNoteRepo(
        roomProvider: RoomProvider,
        noteConverter: NoteConverter,
        rollConverter: RollConverter
    ): INoteRepo {
        return NoteRepo(roomProvider, noteConverter, rollConverter)
    }

    @Provides
    @Singleton
    fun provideRankRepo(roomProvider: RoomProvider, converter: RankConverter): IRankRepo {
        return RankRepo(roomProvider, converter)
    }

    @Provides
    @Singleton
    fun provideBackupRepo(roomProvider: RoomProvider): IBackupRepo {
        return BackupRepo(roomProvider)
    }
}