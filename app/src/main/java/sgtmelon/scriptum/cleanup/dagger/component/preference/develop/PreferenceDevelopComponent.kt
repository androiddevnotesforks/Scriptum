package sgtmelon.scriptum.cleanup.dagger.component.preference.develop

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.develop.infrastructure.screen.develop.DevelopFragment

/**
 * Component for [DevelopFragment].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface PreferenceDevelopComponent {

    fun inject(fragment: DevelopFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): PreferenceDevelopComponent
    }
}