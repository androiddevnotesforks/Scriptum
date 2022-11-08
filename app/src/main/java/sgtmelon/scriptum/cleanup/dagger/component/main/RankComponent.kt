package sgtmelon.scriptum.cleanup.dagger.component.main

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

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