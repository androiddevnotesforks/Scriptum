package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.domain.useCase.bin.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.bin.ClearBinUseCaseImpl

@Module
class BinUseCaseModule {

    @Provides
    fun provideClearBinUseCase(repository: NoteRepo): ClearBinUseCase {
        return ClearBinUseCaseImpl(repository)
    }
}