package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.data.repository.database.BindRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.bind.GetBindNoteListUseCase
import sgtmelon.scriptum.domain.useCase.bind.GetNotificationCountUseCase
import sgtmelon.scriptum.domain.useCase.bind.UnbindNoteUseCase

@Module
class BindUseCaseModule {

    @Provides
    fun provideGetBindNoteListUseCase(
        preferencesRepo: PreferencesRepo,
        noteRepo: NoteRepo
    ): GetBindNoteListUseCase {
        return GetBindNoteListUseCase(preferencesRepo, noteRepo)
    }

    @Provides
    fun provideGetNotificationCountUseCase(repository: BindRepo): GetNotificationCountUseCase {
        return GetNotificationCountUseCase(repository)
    }

    @Provides
    fun provideUnbindNoteUseCase(repository: BindRepo): UnbindNoteUseCase {
        return UnbindNoteUseCase(repository)
    }
}