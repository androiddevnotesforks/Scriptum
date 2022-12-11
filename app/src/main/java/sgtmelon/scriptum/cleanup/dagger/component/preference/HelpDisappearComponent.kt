package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.preference.disappear.HelpDisappearActivity

/**
 * Component for [HelpDisappearActivity].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface HelpDisappearComponent {

    fun inject(activity: HelpDisappearActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): HelpDisappearComponent
    }
}