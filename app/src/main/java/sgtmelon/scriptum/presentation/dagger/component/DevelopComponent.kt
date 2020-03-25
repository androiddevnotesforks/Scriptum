package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.DevelopActivity

/**
 * Component for [DevelopActivity].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class])
interface DevelopComponent {

    fun inject(activity: DevelopActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: DevelopActivity): Builder

        fun build(): DevelopComponent
    }

}