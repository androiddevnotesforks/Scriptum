package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNoteListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase

@Module
class MainUseCaseModule {

    @Provides
    fun provideGetNoteListUseCase(
        preferencesRepo: PreferencesRepo,
        noteRepo: NoteRepo
    ): GetNoteListUseCase {
        return GetNoteListUseCase(preferencesRepo, noteRepo)
    }

    @Provides
    fun provideSortNoteListUseCase(): SortNoteListUseCase {
        return SortNoteListUseCase()
    }

    @Provides
    fun provideClearBinUseCase(repository: NoteRepo): ClearBinUseCase {
        return ClearBinUseCase(repository)
    }
}