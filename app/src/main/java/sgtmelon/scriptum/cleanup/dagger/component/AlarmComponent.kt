package sgtmelon.scriptum.cleanup.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.AlarmModule
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity

/**
 * Component for [AlarmActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
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