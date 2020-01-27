package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.DevelopModule
import sgtmelon.scriptum.screen.ui.DevelopActivity

@ActivityScope
@Subcomponent(modules = [DevelopModule::class])
interface DevelopComponent {

    fun inject(activity: DevelopActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: DevelopActivity): Builder

        fun build(): DevelopComponent
    }

}