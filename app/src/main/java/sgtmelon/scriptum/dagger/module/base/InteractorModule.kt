package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.interactor.notification.SignalInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo

@Module
class InteractorModule {

    @Provides
    @ActivityScope
    fun provideBindInteractor(preferenceRepo: IPreferenceRepo, bindRepo: IBindRepo,
                              rankRepo: IRankRepo, noteRepo: INoteRepo): IBindInteractor {
        return BindInteractor(preferenceRepo, bindRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideSignalInteractor(context: Context,
                                preferenceRepo: IPreferenceRepo): ISignalInteractor {
        return SignalInteractor(context, preferenceRepo)
    }

}