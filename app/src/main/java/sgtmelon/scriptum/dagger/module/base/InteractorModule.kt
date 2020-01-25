package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor

@Module
class InteractorModule {

    @Provides
    @ActivityScope
    fun provideBindInteractor(context: Context): IBindInteractor = BindInteractor(context)

}