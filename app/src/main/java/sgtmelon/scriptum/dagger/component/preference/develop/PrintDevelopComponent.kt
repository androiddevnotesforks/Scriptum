package sgtmelon.scriptum.dagger.component.preference.develop

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.PrintModule
import sgtmelon.scriptum.dagger.module.base.ControlModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.PrintDevelopActivity

/**
 * Component for [PrintDevelopActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    ControlModule::class,
    PrintModule::class
])
interface PrintDevelopComponent {

    fun inject(activity: PrintDevelopActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: PrintDevelopActivity): Builder

        fun build(): PrintDevelopComponent
    }
}