package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.MainModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.screen.ui.main.MainActivity

@ActivityScope
@Subcomponent(modules = [InteractorModule::class, MainModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: MainActivity): Builder

        fun build(): MainComponent
    }

}