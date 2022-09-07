package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCaseImpl
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCaseImpl
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCaseImpl
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCaseImpl

@Module
class NoteUseCaseModule {

    @Provides
    fun provideGetCopyTextUseCase(repository: NoteRepo): GetCopyTextUseCase {
        return GetCopyTextUseCaseImpl(repository)
    }

    @Provides
    fun provideClearNoteUseCase(repository: NoteRepo): ClearNoteUseCase {
        return ClearNoteUseCaseImpl(repository)
    }

    @Provides
    fun provideDeleteNoteUseCase(repository: NoteRepo): DeleteNoteUseCase {
        return DeleteNoteUseCaseImpl(repository)
    }

    @Provides
    fun provideRestoreNoteUseCase(repository: NoteRepo): RestoreNoteUseCase {
        return RestoreNoteUseCaseImpl(repository)
    }
}