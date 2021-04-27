package sgtmelon.scriptum.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.presentation.service.EternalService
import sgtmelon.scriptum.presentation.service.presenter.EternalPresenter
import sgtmelon.scriptum.presentation.service.presenter.IEternalPresenter

/**
 * Module for provide presenter's
 */
@Module
class PresenterModule {

    @Provides
    @ActivityScope
    fun provideEternalPresenter(
        service: EternalService
    ): IEternalPresenter {
        return EternalPresenter().apply {
            setCallback(service)
        }
    }
}