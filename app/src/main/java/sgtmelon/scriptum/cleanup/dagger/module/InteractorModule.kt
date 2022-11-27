package sgtmelon.scriptum.cleanup.dagger.module

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractorImpl

@Module
class InteractorModule {

    //region Main

    @Provides
    @ActivityScope
    fun provideNotesInteractor(noteRepo: NoteRepo): INotesInteractor {
        return NotesInteractor(noteRepo)
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideTextNoteInteractor(
        rankRepo: RankRepo,
        noteRepo: NoteRepo
    ): ITextNoteInteractor {
        return TextNoteInteractor(rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideRollNoteInteractor(
        rankRepo: RankRepo,
        noteRepo: NoteRepo
    ): IRollNoteInteractor {
        return RollNoteInteractor(rankRepo, noteRepo)
    }

    //endregion

    //region Preference

    @Provides
    @ActivityScope
    fun provideDevelopInteractor(repository: DevelopRepo): DevelopInteractor {
        return DevelopInteractorImpl(repository)
    }

    //endregion

}