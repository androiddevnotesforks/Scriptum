package sgtmelon.scriptum.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.base.ControlModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment

/**
 * Component for [PreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    ControlModule::class
])
interface PreferenceComponent {

    fun inject(fragment: PreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: PreferenceFragment): Builder

        fun build(): PreferenceComponent
    }
}