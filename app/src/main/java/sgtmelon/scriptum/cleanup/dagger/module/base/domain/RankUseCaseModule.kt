package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.domain.useCase.rank.CorrectPositionsUseCase

@Module
class RankUseCaseModule {

    @Provides
    fun provideCorrectPositionsUseCase(): CorrectPositionsUseCase {
        return CorrectPositionsUseCase()
    }
}