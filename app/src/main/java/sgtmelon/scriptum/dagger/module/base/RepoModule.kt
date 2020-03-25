package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.data.repository.room.*
import sgtmelon.scriptum.data.repository.room.callback.*
import javax.inject.Singleton

/**
 * Module for provide repo's
 */
@Module
class RepoModule {

    @Provides
    @Singleton
    fun providePreferenceRepo(context: Context): IPreferenceRepo = PreferenceRepo(context)


    @Provides
    @Singleton
    fun provideAlarmRepo(context: Context): IAlarmRepo = AlarmRepo(context)

    @Provides
    @Singleton
    fun provideBindRepo(context: Context): IBindRepo = BindRepo(context)

    @Provides
    @Singleton
    fun provideDevelopRepo(context: Context): IDevelopRepo = DevelopRepo(context)

    @Provides
    @Singleton
    fun provideNoteRepo(context: Context): INoteRepo = NoteRepo(context)

    @Provides
    @Singleton
    fun provideRankRepo(context: Context): IRankRepo = RankRepo(context)

}