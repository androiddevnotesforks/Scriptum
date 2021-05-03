package sgtmelon.scriptum.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.base.ControlModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPrefFragment

/**
 * Component for [AlarmPrefFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    ControlModule::class
])
interface AlarmPrefComponent {

    fun inject(fragment: AlarmPrefFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: AlarmPrefFragment): Builder

        fun build(): AlarmPrefComponent
    }
}