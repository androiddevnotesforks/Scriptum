package sgtmelon.scriptum.cleanup.dagger.component.main

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity

/**
 * Component for [MainActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: MainActivity): Builder

        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): MainComponent
    }
}