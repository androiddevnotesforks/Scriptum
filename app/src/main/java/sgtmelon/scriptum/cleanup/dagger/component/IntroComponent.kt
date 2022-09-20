package sgtmelon.scriptum.cleanup.dagger.component

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity

/**
 * Component for [IntroActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface IntroComponent {

    fun inject(activity: IntroActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: IntroActivity): Builder

        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): IntroComponent
    }
}