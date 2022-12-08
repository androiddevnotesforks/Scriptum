package sgtmelon.scriptum.cleanup.dagger.module

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractorImpl

@Deprecated("Convert to use cases if possible")
@Module
class InteractorModule {

    //region Note

    @Provides
    @ActivityScope
    fun provideTextNoteInteractor(repository: NoteRepo): ITextNoteInteractor {
        return TextNoteInteractor(repository)
    }

    @Provides
    @ActivityScope
    fun provideRollNoteInteractor(repository: NoteRepo): IRollNoteInteractor {
        return RollNoteInteractor(repository)
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