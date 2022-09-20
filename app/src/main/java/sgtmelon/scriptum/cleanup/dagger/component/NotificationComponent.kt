package sgtmelon.scriptum.cleanup.dagger.component

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity

/**
 * Component for [NotificationActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface NotificationComponent {

    fun inject(activity: NotificationActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: NotificationActivity): Builder

        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): NotificationComponent
    }
}