package sgtmelon.scriptum.dagger.component.main

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment

/**
 * Component for [RankFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface RankComponent {

    fun inject(fragment: RankFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: RankFragment): Builder

        fun build(): RankComponent
    }
}