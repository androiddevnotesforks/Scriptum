package sgtmelon.scriptum.dagger.component.preference.develop

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.DevelopFragment

/**
 * Component for [DevelopFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface DevelopPreferenceComponent {

    fun inject(fragment: DevelopFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: DevelopFragment): Builder

        fun build(): DevelopPreferenceComponent
    }
}