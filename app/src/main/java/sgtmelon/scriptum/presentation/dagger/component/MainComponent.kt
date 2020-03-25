package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.MainModule
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity

/**
 * Component for [MainActivity].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class, MainModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: MainActivity): Builder

        fun build(): MainComponent
    }

}