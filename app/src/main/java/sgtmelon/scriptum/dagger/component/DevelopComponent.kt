package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.DevelopModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.DevelopFragment

/**
 * Component for [DevelopFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    DevelopModule::class
])
interface DevelopComponent {

    fun inject(fragment: DevelopFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: DevelopFragment): Builder

        fun build(): DevelopComponent
    }
}