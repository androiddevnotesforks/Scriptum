package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.BinModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.screen.ui.main.BinFragment

@ActivityScope
@Subcomponent(modules = [BinModule::class])
interface BinComponent {

    fun inject(fragment: BinFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: BinFragment): Builder

        fun build(): BinComponent
    }

}