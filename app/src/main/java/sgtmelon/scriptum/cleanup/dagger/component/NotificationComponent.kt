package sgtmelon.scriptum.cleanup.dagger.component

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity

/**
 * Component for [NotificationsActivity].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface NotificationComponent {

    fun inject(activity: NotificationsActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): NotificationComponent
    }
}