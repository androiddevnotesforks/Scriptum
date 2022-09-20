package sgtmelon.scriptum.cleanup.dagger.module.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepoImpl
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter

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
}