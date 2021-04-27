package sgtmelon.scriptum.dagger.component.service

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.PresenterModule
import sgtmelon.scriptum.presentation.service.EternalService

/**
 * Component for [EternalService].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    PresenterModule::class
])
interface EternalComponent {

    fun inject(service: EternalService)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(service: EternalService): Builder

        fun build(): EternalComponent
    }
}