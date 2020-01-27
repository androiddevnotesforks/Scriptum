package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.SplashModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.screen.ui.SplashActivity

@ActivityScope
@Subcomponent(modules = [SplashModule::class])
interface SplashComponent {

    fun inject(activity: SplashActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: SplashActivity): Builder

        fun build(): SplashComponent
    }

}