package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter

@Module
class ConverterModule {

    //region Model converters

    @Provides
    fun provideAlarmConverter() = AlarmConverter()

    @Provides
    fun provideNoteConverter() = NoteConverter()

    @Provides
    fun provideRankConverter() = RankConverter()

    @Provides
    fun provideRollConverter() = RollConverter()

    //endregion

    //region Type converters

    @Provides
    fun provideStringConverter() = StringConverter()

    @Provides
    fun provideNoteTypeConverter() = NoteTypeConverter()

    //endregion

    @Provides
    fun provideThemeConverter() = ThemeConverter()

    @Provides
    fun provideSortConverter() = SortConverter()

    @Provides
    fun provideColorConverter() = ColorConverter()

    @Provides
    fun provideSavePeriodConverter() = SavePeriodConverter()

    @Provides
    fun provideRepeatConverter() = RepeatConverter()

    @Provides
    fun provideSignalConverter() = SignalConverter()

}