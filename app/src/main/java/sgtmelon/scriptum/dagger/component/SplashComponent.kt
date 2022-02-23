package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity

/**
 * Component for [SplashActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface SplashComponent {

    fun inject(activity: SplashActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: SplashActivity): Builder

        fun build(): SplashComponent
    }
}