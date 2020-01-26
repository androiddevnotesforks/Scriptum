package sgtmelon.scriptum.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo

@Module
class InteractorModule {

    @Provides
    @ActivityScope
    fun provideBindInteractor(iPreferenceRepo: IPreferenceRepo, iBindRepo: IBindRepo,
                              iRankRepo: IRankRepo, iNoteRepo: INoteRepo): IBindInteractor {
        return BindInteractor(iPreferenceRepo, iBindRepo, iRankRepo, iNoteRepo)
    }

}