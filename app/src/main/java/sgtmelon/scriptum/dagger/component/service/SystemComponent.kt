package sgtmelon.scriptum.dagger.component.service

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.PresenterModule
import sgtmelon.scriptum.presentation.screen.system.SystemLogic

/**
 * Component for [SystemLogic].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    PresenterModule::class
])
interface SystemComponent {

    fun inject(logic: SystemLogic)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(logic: SystemLogic): Builder

        fun build(): SystemComponent
    }
}