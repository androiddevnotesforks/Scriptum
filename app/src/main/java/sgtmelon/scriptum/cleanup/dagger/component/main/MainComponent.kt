package sgtmelon.scriptum.cleanup.dagger.component.main

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.MainModule
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity

/**
 * Component for [MainActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    MainModule::class
])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: MainActivity): Builder

        fun build(): MainComponent
    }
}