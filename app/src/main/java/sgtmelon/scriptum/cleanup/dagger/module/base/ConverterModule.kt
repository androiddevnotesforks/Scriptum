package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.infrastructure.converter.SignalConverter

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
    fun provideSignalConverter() = SignalConverter()

    @Provides
    @Singleton
    fun provideNoteTypeConverter() = NoteTypeConverter()

    //endregion

}