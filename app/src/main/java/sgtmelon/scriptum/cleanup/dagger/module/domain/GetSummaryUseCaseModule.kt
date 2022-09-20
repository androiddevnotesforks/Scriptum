package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetDefaultColorSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetRepeatSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSavePeriodSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSortSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetThemeSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetVolumeSummaryUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter

@Module
class GetSummaryUseCaseModule {

    @Provides
    @Named("Theme")
    fun provideGetThemeSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: ThemeConverter
    ): GetSummaryUseCase {
        return GetThemeSummaryUseCase(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("Sort")
    fun provideGetSortSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: SortConverter
    ): GetSummaryUseCase {
        return GetSortSummaryUseCase(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("DefaultColor")
    fun provideGetDefaultColorSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: ColorConverter
    ): GetSummaryUseCase {
        return GetDefaultColorSummaryUseCase(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("SavePeriod")
    fun provideGetSavePeriodSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: SavePeriodConverter
    ): GetSummaryUseCase {
        return GetSavePeriodSummaryUseCase(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("Repeat")
    fun provideGetRepeatSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: RepeatConverter
    ): GetSummaryUseCase {
        return GetRepeatSummaryUseCase(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("Volume")
    fun provideGetVolumeSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo
    ): GetSummaryUseCase {
        return GetVolumeSummaryUseCase(summaryDataSource, preferencesRepo)
    }

    @Provides
    fun provideGetSignalSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo
    ) : GetSignalSummaryUseCase {
        return GetSignalSummaryUseCase(summaryDataSource, preferencesRepo)
    }
}