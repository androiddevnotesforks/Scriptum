package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment

/**
 * Component for [AlarmPreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface AlarmPreferenceComponent {

    fun inject(fragment: AlarmPreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): AlarmPreferenceComponent
    }
}