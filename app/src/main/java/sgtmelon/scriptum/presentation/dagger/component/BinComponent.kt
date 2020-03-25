package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment

/**
 * Component for [BinFragment].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class])
interface BinComponent {

    fun inject(fragment: BinFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: BinFragment): Builder

        fun build(): BinComponent
    }

}