package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetThemeSummaryUseCaseImpl
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter

@Module
class GetSummaryUseCaseModule {

    @Provides
    @Named("Theme")
    fun provideGetThemeSummaryUseCase(
        summaryProvider: SummaryProvider,
        preferencesRepo: PreferencesRepo,
        themeConverter: ThemeConverter
    ): GetSummaryUseCase {
        return GetThemeSummaryUseCaseImpl(summaryProvider, preferencesRepo, themeConverter)
    }
}