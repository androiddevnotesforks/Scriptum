package sgtmelon.scriptum.dagger.component.preference.develop

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.ServiceDevelopFragment

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
        fun set(fragment: ServiceDevelopFragment): Builder

        fun build(): ServiceDevelopComponent
    }
}