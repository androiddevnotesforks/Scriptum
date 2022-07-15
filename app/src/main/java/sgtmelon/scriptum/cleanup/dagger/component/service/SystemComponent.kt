package sgtmelon.scriptum.cleanup.dagger.component.service

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.PresenterModule
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic

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