package sgtmelon.scriptum.cleanup.dagger.module.base.infrastructure

import android.content.Context
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Module for provide [RoomDatabase] staff.
 */
@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database = Database[context]

    @Provides
    @Singleton
    fun provideNoteDao(db: Database) = db.noteDao

    @Provides
    @Singleton
    fun provideRollDao(db: Database) = db.rollDao

    @Provides
    @Singleton
    fun provideRollVisibleDao(db: Database) = db.rollVisibleDao

    @Provides
    @Singleton
    fun provideRankDao(db: Database) = db.rankDao

    @Provides
    @Singleton
    fun provideAlarmDao(db: Database) = db.alarmDao
}