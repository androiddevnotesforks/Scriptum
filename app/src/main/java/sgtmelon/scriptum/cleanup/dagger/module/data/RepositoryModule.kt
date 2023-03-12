package sgtmelon.scriptum.cleanup.dagger.module.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.data.repository.database.AlarmRepoImpl
import sgtmelon.scriptum.data.repository.database.BindRepo
import sgtmelon.scriptum.data.repository.database.BindRepoImpl
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepoImpl
import sgtmelon.scriptum.develop.data.DevelopRepo
import sgtmelon.scriptum.develop.data.DevelopRepoImpl
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providePreferencesRepo(
        dataSource: PreferencesDataSource,
        themeConverter: ThemeConverter,
        sortConverter: SortConverter,
        colorConverter: ColorConverter,
        savePeriodConverter: SavePeriodConverter,
        repeatConverter: RepeatConverter,
        signalConverter: SignalConverter
    ): PreferencesRepo {
        return PreferencesRepoImpl(
            dataSource,
            themeConverter, sortConverter, colorConverter,
            savePeriodConverter, repeatConverter, signalConverter
        )
    }

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
        alarmDataSource: AlarmDataSource,
        key: PreferencesKeyProvider,
        def: PreferencesDefProvider,
        preferences: Preferences,
        fileDataSource: FileDataSource
    ): DevelopRepo {
        return DevelopRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource,
            rankDataSource, alarmDataSource,
            key, def, preferences, fileDataSource
        )
    }

}