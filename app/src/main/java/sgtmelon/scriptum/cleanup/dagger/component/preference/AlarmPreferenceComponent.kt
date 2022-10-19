package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment

/**
 * Component for [AlarmPreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface AlarmPreferenceComponent {

    fun inject(fragment: AlarmPreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(lifecycle: Lifecycle): Builder

        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): AlarmPreferenceComponent
    }
}