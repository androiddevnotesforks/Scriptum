package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import javax.inject.Named
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
import sgtmelon.scriptum.infrastructure.provider.SummaryProviderImpl

@Module
class GetSummaryUseCaseModule {

    @Provides
    @Named("Theme")
    fun provideGetThemeSummaryUseCase(
        summaryProvider: SummaryProviderImpl,
        preferencesRepo: PreferencesRepo,
        converter: ThemeConverter
    ): GetSummaryUseCase {
        return GetThemeSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("Sort")
    fun provideGetSortSummaryUseCase(
        summaryProvider: SummaryProviderImpl,
        preferencesRepo: PreferencesRepo,
        converter: SortConverter
    ): GetSummaryUseCase {
        return GetSortSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("DefaultColor")
    fun provideGetDefaultColorSummaryUseCase(
        summaryProvider: SummaryProviderImpl,
        preferencesRepo: PreferencesRepo,
        converter: ColorConverter
    ): GetSummaryUseCase {
        return GetDefaultColorSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("SavePeriod")
    fun provideGetSavePeriodSummaryUseCase(
        summaryProvider: SummaryProviderImpl,
        preferencesRepo: PreferencesRepo,
        converter: SavePeriodConverter
    ): GetSummaryUseCase {
        return GetSavePeriodSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("Repeat")
    fun provideGetRepeatSummaryUseCase(
        summaryProvider: SummaryProviderImpl,
        preferencesRepo: PreferencesRepo,
        converter: RepeatConverter
    ): GetSummaryUseCase {
        return GetRepeatSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Provides
    @Named("Volume")
    fun provideGetVolumeSummaryUseCase(
        summaryProvider: SummaryProviderImpl,
        preferencesRepo: PreferencesRepo
    ): GetSummaryUseCase {
        return GetVolumeSummaryUseCaseImpl(summaryProvider, preferencesRepo)
    }

    @Provides
    fun provideGetSignalSummaryUseCase(
        summaryProvider: SummaryProviderImpl,
        preferencesRepo: PreferencesRepo
    ) : GetSignalSummaryUseCase {
        return GetSignalSummaryUseCaseImpl(summaryProvider, preferencesRepo)
    }
}