package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.infrastructure.preferences.PreferenceProvider
import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.infrastructure.preferences.IPreferenceRepo
import sgtmelon.scriptum.infrastructure.preferences.PreferenceRepo
import sgtmelon.scriptum.cleanup.data.repository.room.*
import sgtmelon.scriptum.cleanup.data.repository.room.callback.*
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import javax.inject.Singleton

/**
 * Module for provide repo's
 */
@Module
class RepoModule {

    @Provides
    @Singleton
    fun providePreferenceRepo(
        keyProvider: PreferenceProvider.Key,
        defProvider: PreferenceProvider.Def,
        preferences: SharedPreferences
    ): IPreferenceRepo {
        return PreferenceRepo(keyProvider, defProvider, preferences)
    }


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