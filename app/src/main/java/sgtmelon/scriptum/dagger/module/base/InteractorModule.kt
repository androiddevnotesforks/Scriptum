package sgtmelon.scriptum.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.interactor.main.MainInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.ui.main.MainActivity

@Module(includes = [RepoModule::class])
class InteractorModule {

    @Provides
    @ActivityScope
    fun provideBindInteractor(iPreferenceRepo: IPreferenceRepo, iBindRepo: IBindRepo,
                              iRankRepo: IRankRepo, iNoteRepo: INoteRepo): IBindInteractor {
        return BindInteractor(iPreferenceRepo, iBindRepo, iRankRepo, iNoteRepo)
    }

    @Provides
    @ActivityScope
    fun provideMainInteractor(iAlarmRepo: IAlarmRepo, activity: MainActivity): IMainInteractor {
        return MainInteractor(iAlarmRepo, activity)
    }

}