package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.domain.useCase.rank.CorrectPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.CorrectPositionsUseCaseImpl

@Module
class RankUseCaseModule {

    @Provides
    fun provideCorrectPositionsUseCase(): CorrectPositionsUseCase {
        return CorrectPositionsUseCaseImpl()
    }
}