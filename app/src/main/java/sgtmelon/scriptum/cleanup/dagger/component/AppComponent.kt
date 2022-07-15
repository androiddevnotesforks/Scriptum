package sgtmelon.scriptum.cleanup.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.AppModule
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.AppActivity

/**
 * Component for [AppActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    AppModule::class
])
interface AppComponent {

    fun inject(activity: AppActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: AppActivity): Builder

        fun build(): AppComponent
    }
}