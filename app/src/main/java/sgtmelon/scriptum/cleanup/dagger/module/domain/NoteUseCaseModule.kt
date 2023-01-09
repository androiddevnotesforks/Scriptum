package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollCheckUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollVisibleUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetTextNoteUseCase

@Module
class NoteUseCaseModule {

    @Provides
    fun provideGetCopyTextUseCase(repository: NoteRepo): GetCopyTextUseCase {
        return GetCopyTextUseCase(repository)
    }

    @Provides
    fun provideCreateTextNoteUseCase(repository: PreferencesRepo): CreateTextNoteUseCase {
        return CreateTextNoteUseCase(repository)
    }

    @Provides
    fun provideCreateRollNoteUseCase(repository: PreferencesRepo): CreateRollNoteUseCase {
        return CreateRollNoteUseCase(repository)
    }

    @Provides
    fun provideGetTextNoteUseCase(repository: NoteRepo): GetTextNoteUseCase {
        return GetTextNoteUseCase(repository)
    }

    @Provides
    fun provideGetRollNoteUseCase(repository: NoteRepo): GetRollNoteUseCase {
        return GetRollNoteUseCase(repository)
    }

    @Provides
    fun provideCacheTextNoteUseCase(): CacheTextNoteUseCase {
        return CacheTextNoteUseCase()
    }

    @Provides
    fun provideCacheRollNoteUseCase(): CacheRollNoteUseCase {
        return CacheRollNoteUseCase()
    }

    @Provides
    fun provideSaveNoteUseCase(noteRepo: NoteRepo, rankRepo: RankRepo): SaveNoteUseCase {
        return SaveNoteUseCase(noteRepo, rankRepo)
    }

    @Provides
    fun provideConvertNoteUseCase(repository: NoteRepo): ConvertNoteUseCase {
        return ConvertNoteUseCase(repository)
    }

    @Provides
    fun provideUpdateNoteUseCase(repository: NoteRepo): UpdateNoteUseCase {
        return UpdateNoteUseCase(repository)
    }

    @Provides
    fun provideDeleteNoteUseCase(repository: NoteRepo): DeleteNoteUseCase {
        return DeleteNoteUseCase(repository)
    }

    @Provides
    fun provideRestoreNoteUseCase(repository: NoteRepo): RestoreNoteUseCase {
        return RestoreNoteUseCase(repository)
    }

    @Provides
    fun provideClearNoteUseCase(repository: NoteRepo): ClearNoteUseCase {
        return ClearNoteUseCase(repository)
    }

    //region Roll use cases

    @Provides
    fun provideUpdateRollVisibleUseCase(repository: NoteRepo): UpdateRollVisibleUseCase {
        return UpdateRollVisibleUseCase(repository)
    }

    @Provides
    fun provideUpdateRollCheckUseCase(repository: NoteRepo): UpdateRollCheckUseCase {
        return UpdateRollCheckUseCase(repository)
    }

    //endregion
}