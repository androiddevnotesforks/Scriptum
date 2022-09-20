package sgtmelon.scriptum.cleanup.dagger.component.main

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment

/**
 * Component for [BinFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface BinComponent {

    fun inject(fragment: BinFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: BinFragment): Builder

        fun build(): BinComponent
    }
}