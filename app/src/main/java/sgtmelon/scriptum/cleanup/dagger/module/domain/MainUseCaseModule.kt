package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetBinListUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNotesListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase

@Module
class MainUseCaseModule {

    @Provides
    fun provideGetBinListUseCase(
        preferencesRepo: PreferencesRepo,
        noteRepo: NoteRepo
    ): GetBinListUseCase {
        return GetBinListUseCase(preferencesRepo, noteRepo)
    }

    @Provides
    fun provideGetNotesListUseCase(
        preferencesRepo: PreferencesRepo,
        noteRepo: NoteRepo
    ): GetNotesListUseCase {
        return GetNotesListUseCase(preferencesRepo, noteRepo)
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