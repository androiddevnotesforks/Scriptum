package sgtmelon.scriptum.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.base.ControlModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment

/**
 * Component for [AlarmPreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    ControlModule::class
])
interface AlarmPreferenceComponent {

    fun inject(fragment: AlarmPreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: AlarmPreferenceFragment): Builder

        fun build(): AlarmPreferenceComponent
    }
}