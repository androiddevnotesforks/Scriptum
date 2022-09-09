package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCaseImpl

@Module
class MainUseCaseModule {

    @Provides
    fun provideClearBinUseCase(repository: NoteRepo): ClearBinUseCase {
        return ClearBinUseCaseImpl(repository)
    }
}