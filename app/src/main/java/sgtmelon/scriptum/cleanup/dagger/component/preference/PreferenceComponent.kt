package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment

/**
 * Component for [MenuPreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface PreferenceComponent {

    fun inject(fragment: MenuPreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): PreferenceComponent
    }
}