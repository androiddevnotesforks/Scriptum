package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.NotificationModule
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity

/**
 * Component for [NotificationActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class, ViewModelModule::class, NotificationModule::class
])
interface NotificationComponent {

    fun inject(activity: NotificationActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: NotificationActivity): Builder

        fun build(): NotificationComponent
    }

}