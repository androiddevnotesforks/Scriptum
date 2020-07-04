package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.AppModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity

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