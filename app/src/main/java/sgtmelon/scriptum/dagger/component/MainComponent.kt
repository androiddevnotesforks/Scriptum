package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Component
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.MainModule
import sgtmelon.scriptum.screen.ui.main.MainActivity

@ActivityScope
@Component(modules = [MainModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance fun set(activity: MainActivity): Builder

        fun build(): MainComponent
    }

}