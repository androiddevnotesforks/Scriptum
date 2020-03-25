package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity

/**
 * Component for [SplashActivity].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class])
interface SplashComponent {

    fun inject(activity: SplashActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: SplashActivity): Builder

        fun build(): SplashComponent
    }

}