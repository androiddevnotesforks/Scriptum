package sgtmelon.scriptum.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.room.converter.model.*
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.data.room.converter.type.StringConverter
import javax.inject.Singleton

/**
 * Module for provide converters
 */
@Module
class ConverterModule {

    //region Model converters

    @Provides
    @Singleton
    fun provideAlarmConverter() = AlarmConverter()

    @Provides
    @Singleton
    fun provideNoteConverter() = NoteConverter()

    @Provides
    @Singleton
    fun provideRankConverter() = RankConverter()

    @Provides
    @Singleton
    fun provideRollConverter() = RollConverter()

    //endregion

    //region Type converters

    @Provides
    @Singleton
    fun provideStringConverter() = StringConverter()

    @Provides
    @Singleton
    fun provideIntConverter() = IntConverter()

    @Provides
    @Singleton
    fun provideNoteTypeConverter() = NoteTypeConverter()

    //endregion

}