package sgtmelon.scriptum.cleanup.dagger.component.preference.develop

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.develop.screen.print.ServiceDevelopFragment

/**
 * Component for [ServiceDevelopFragment]
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface ServiceDevelopComponent {

    fun inject(fragment: ServiceDevelopFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): ServiceDevelopComponent
    }
}