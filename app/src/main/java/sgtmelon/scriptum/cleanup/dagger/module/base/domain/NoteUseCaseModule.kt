package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase

@Module
class NoteUseCaseModule {

    @Provides
    fun provideGetCopyTextUseCase(repository: NoteRepo): GetCopyTextUseCase {
        return GetCopyTextUseCase(repository)
    }

    @Provides
    fun provideClearNoteUseCase(repository: NoteRepo): ClearNoteUseCase {
        return ClearNoteUseCase(repository)
    }

    @Provides
    fun provideDeleteNoteUseCase(repository: NoteRepo): DeleteNoteUseCase {
        return DeleteNoteUseCase(repository)
    }

    @Provides
    fun provideRestoreNoteUseCase(repository: NoteRepo): RestoreNoteUseCase {
        return RestoreNoteUseCase(repository)
    }
}