package sgtmelon.scriptum.cleanup.dagger.module.base.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepoImpl
import sgtmelon.scriptum.infrastructure.converter.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.converter.SortConverter
import sgtmelon.scriptum.infrastructure.converter.ThemeConverter

@Module
class RepositoryModule {

    @Singleton
    @Provides
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