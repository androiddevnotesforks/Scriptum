package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity

/**
 * Component for [IntroActivity].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class])
interface IntroComponent {

    fun inject(activity: IntroActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: IntroActivity): Builder

        fun build(): IntroComponent
    }

}