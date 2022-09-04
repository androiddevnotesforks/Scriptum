package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.SystemPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic

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