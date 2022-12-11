package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.develop.data.DevelopRepo
import sgtmelon.scriptum.develop.domain.GetPrintListUseCase
import sgtmelon.scriptum.develop.domain.GetRandomNoteIdUseCase
import sgtmelon.scriptum.develop.domain.ResetPreferencesUseCase

@Module
class DevelopUseCaseModule {

    @Provides
    fun provideGetPrintListUseCase(repository: DevelopRepo): GetPrintListUseCase {
        return GetPrintListUseCase(repository)
    }

    @Provides
    fun provideGetRandomNoteIdUseCase(repository: DevelopRepo): GetRandomNoteIdUseCase {
        return GetRandomNoteIdUseCase(repository)
    }

    @Provides
    fun provideResetPreferencesUseCase(repository: DevelopRepo): ResetPreferencesUseCase {
        return ResetPreferencesUseCase(repository)
    }
}