package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment

/**
 * Component for [RankFragment].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class])
interface RankComponent {

    fun inject(fragment: RankFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: RankFragment): Builder

        fun build(): RankComponent
    }

}