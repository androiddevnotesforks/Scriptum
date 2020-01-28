package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.AppModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.screen.ui.AppActivity

@ActivityScope
@Subcomponent(modules = [InteractorModule::class, AppModule::class])
interface AppComponent {

    fun inject(activity: AppActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: AppActivity): Builder

        fun build(): AppComponent
    }

}