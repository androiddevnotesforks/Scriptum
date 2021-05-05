package sgtmelon.scriptum.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.presentation.screen.presenter.system.SystemPresenter
import sgtmelon.scriptum.presentation.screen.system.SystemLogic

/**
 * Module for provide presenter's
 */
@Module
class PresenterModule {

    @Provides
    @ActivityScope
    fun provideEternalPresenter(
        logic: SystemLogic,
        interactor: ISystemInteractor
    ): ISystemPresenter {
        return SystemPresenter(interactor).apply { setCallback(logic) }
    }
}