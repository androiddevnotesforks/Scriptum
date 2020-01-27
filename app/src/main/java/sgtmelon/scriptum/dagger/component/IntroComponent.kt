package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.IntroModule
import sgtmelon.scriptum.screen.ui.intro.IntroActivity

@ActivityScope
@Subcomponent(modules = [IntroModule::class])
interface IntroComponent {

    fun inject(activity: IntroActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: IntroActivity): Builder

        fun build(): IntroComponent
    }

}