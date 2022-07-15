package sgtmelon.scriptum.cleanup.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity

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