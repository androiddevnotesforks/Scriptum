package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetDefaultColorSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.GetRepeatSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.GetSavePeriodSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetSignalSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.GetSortSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetThemeSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.GetVolumeSummaryUseCaseImpl
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
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo,
        converter: ThemeConverter
    ): GetSummaryUseCase {
        return GetThemeSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("Sort")
    fun provideGetSortSummaryUseCase(
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo,
        converter: SortConverter
    ): GetSummaryUseCase {
        return GetSortSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("DefaultColor")
    fun provideGetDefaultColorSummaryUseCase(
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo,
        converter: ColorConverter
    ): GetSummaryUseCase {
        return GetDefaultColorSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("SavePeriod")
    fun provideGetSavePeriodSummaryUseCase(
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo,
        converter: SavePeriodConverter
    ): GetSummaryUseCase {
        return GetSavePeriodSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("Repeat")
    fun provideGetRepeatSummaryUseCase(
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo,
        converter: RepeatConverter
    ): GetSummaryUseCase {
        return GetRepeatSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("Volume")
    fun provideGetVolumeSummaryUseCase(
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo
    ): GetSummaryUseCase {
        return GetVolumeSummaryUseCaseImpl(summaryProvider, preferencesRepo)
    }

    @Provides
    fun provideGetSignalSummaryUseCase(
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo
    ) : GetSignalSummaryUseCase {
        return GetSignalSummaryUseCaseImpl(summaryProvider, preferencesRepo)
    }
}