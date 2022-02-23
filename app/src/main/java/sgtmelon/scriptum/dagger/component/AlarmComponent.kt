package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.AlarmModule
import sgtmelon.scriptum.dagger.module.base.ControlModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity

/**
 * Component for [AlarmActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    ControlModule::class,
    AlarmModule::class
])
interface AlarmComponent {

    fun inject(activity: AlarmActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: AlarmActivity): Builder

        fun build(): AlarmComponent
    }
}