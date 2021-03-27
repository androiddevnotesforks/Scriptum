package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.PrintModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PrintActivity

/**
 * Component for [PrintActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    PrintModule::class
])
interface PrintComponent {

    fun inject(activity: PrintActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: PrintActivity): Builder

        fun build(): PrintComponent
    }
}