package sgtmelon.scriptum.cleanup.dagger.module

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.system.SystemInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractorImpl

@Module
class InteractorModule {

    //region Main

    @Provides
    @ActivityScope
    fun provideRankInteractor(rankRepo: RankRepo): IRankInteractor = RankInteractor(rankRepo)


    @Provides
    @ActivityScope
    fun provideNotesInteractor(noteRepo: NoteRepo): INotesInteractor {
        return NotesInteractor(noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideBinInteractor(noteRepo: NoteRepo): IBinInteractor {
        return BinInteractor(noteRepo)
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

    @Provides
    @ActivityScope
    fun provideSystemInteractor(
        logic: SystemLogic,
        preferencesRepo: PreferencesRepo,
        bindRepo: BindRepo,
        alarmRepo: AlarmRepo,
        rankRepo: RankRepo,
        noteRepo: NoteRepo
    ): ISystemInteractor {
        return SystemInteractor(preferencesRepo, bindRepo, alarmRepo, rankRepo, noteRepo, logic)
    }
}