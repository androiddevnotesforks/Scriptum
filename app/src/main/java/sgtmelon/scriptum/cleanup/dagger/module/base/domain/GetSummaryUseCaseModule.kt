package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetDefaultColorSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetRepeatSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSavePeriodSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSortSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetThemeSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetVolumeSummaryUseCaseImpl
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
        return GetThemeSummaryUseCaseImpl(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("Sort")
    fun provideGetSortSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: SortConverter
    ): GetSummaryUseCase {
        return GetSortSummaryUseCaseImpl(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("DefaultColor")
    fun provideGetDefaultColorSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: ColorConverter
    ): GetSummaryUseCase {
        return GetDefaultColorSummaryUseCaseImpl(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("SavePeriod")
    fun provideGetSavePeriodSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: SavePeriodConverter
    ): GetSummaryUseCase {
        return GetSavePeriodSummaryUseCaseImpl(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("Repeat")
    fun provideGetRepeatSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo,
        converter: RepeatConverter
    ): GetSummaryUseCase {
        return GetRepeatSummaryUseCaseImpl(summaryDataSource, preferencesRepo, converter)
    }

    @Provides
    @Named("Volume")
    fun provideGetVolumeSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo
    ): GetSummaryUseCase {
        return GetVolumeSummaryUseCaseImpl(summaryDataSource, preferencesRepo)
    }

    @Provides
    fun provideGetSignalSummaryUseCase(
        summaryDataSource: SummaryDataSource,
        preferencesRepo: PreferencesRepo
    ) : GetSignalSummaryUseCase {
        return GetSignalSummaryUseCaseImpl(summaryDataSource, preferencesRepo)
    }
}