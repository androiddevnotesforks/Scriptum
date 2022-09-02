package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.domain.useCase.database.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.database.note.ClearNoteUseCaseImpl
import sgtmelon.scriptum.domain.useCase.database.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.database.note.DeleteNoteUseCaseImpl
import sgtmelon.scriptum.domain.useCase.database.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.database.note.RestoreNoteUseCaseImpl

@Module
class NoteUseCaseModule {

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